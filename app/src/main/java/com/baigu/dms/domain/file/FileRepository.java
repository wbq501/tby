package com.baigu.dms.domain.file;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;


import com.baigu.dms.BaseApplication;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.view.imagepicker.FileChooser;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.micky.logger.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class FileRepository {

    /**
     * 获取手机系统库里的所有图片目录
     *
     * @return
     */
    public static List<SDFolder> getAllImageFolder() {
        final List<SDFolder> imageFolderList = new ArrayList<SDFolder>();

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        HashSet<String> dirScanedHashSet = new HashSet<String>();
        try {
            cursor = BaseApplication.getContext().getContentResolver().query(imageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);
            String firstImagePath = "";

            while (cursor.moveToNext()) {
                //图片路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //第一张图片
                if (TextUtils.isEmpty(firstImagePath)) {
                    firstImagePath = path;
                }
                //获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                String dir = parentFile.getAbsolutePath();
                SDFolder imageFolder = null;
                if (!dirScanedHashSet.contains(dir.toUpperCase())) {
                    dirScanedHashSet.add(dir.toUpperCase());
                    imageFolder = new SDFolder();
                    imageFolder.setFolderType(FileChooser.FileType.TYPE_IMAGE);
                    imageFolder.setPath(dir);
                    imageFolder.setImageUri(Uri.parse("file://" + path));
                } else {
                    continue;
                }
                int picSize = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg");
                    }
                }).length;
                imageFolder.setCount(picSize);
                if ("Camera".equalsIgnoreCase(imageFolder.getName())) {
//                    imageFolder.setName(BaseApplication.getContext().getString(R.string.camera));
                    imageFolderList.add(0, imageFolder);
                } else {
                    imageFolderList.add(imageFolder);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imageFolderList;
    }

    /**
     * 获取手机系统库里的所有图片目录和目录下的图片
     *
     * @return
     */
    public static Observable<RxOptional<List<SDFolder>>> getImageFolderAndImages() {

        return Observable.create(new ObservableOnSubscribe<RxOptional<List<SDFolder>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RxOptional<List<SDFolder>>> observableEmitter) throws Exception {
                final List<SDFolder> imageFolderList = new ArrayList<SDFolder>();

                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = null;
                HashSet<String> dirScanedHashSet = new HashSet<String>();
                try {
                    cursor = BaseApplication.getContext().getContentResolver().query(imageUri, null,
                            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"},
                            MediaStore.Images.Media.DATE_MODIFIED);
                    String firstImagePath = "";

                    while (cursor.moveToNext()) {
                        //图片路径
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        //第一张图片
                        if (TextUtils.isEmpty(firstImagePath)) {
                            firstImagePath = path;
                        }
                        //获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null) {
                            continue;
                        }
                        String dir = parentFile.getAbsolutePath();
                        SDFolder imageFolder = null;
                        if (!dirScanedHashSet.contains(dir.toUpperCase())) {
                            dirScanedHashSet.add(dir.toUpperCase());
                            imageFolder = new SDFolder();
                            imageFolder.setFolderType(FileChooser.FileType.TYPE_IMAGE);
                            imageFolder.setPath(dir);
                            imageFolder.setImageUri(Uri.parse("file://" + path));
                            List<SDFile> imageList = getImageList(imageFolder);
                            imageFolder.setFileList(imageList);
                        } else {
                            continue;
                        }
                        int imageCount = imageFolder.getFileList() != null ? imageFolder.getFileList().size() : 0;
                        imageFolder.setCount(imageCount);
                        if ("Camera".equalsIgnoreCase(imageFolder.getName())) {
//                            imageFolder.setName(BaseApplication.getContext().getString(R.string.camera));
                            imageFolderList.add(0, imageFolder);
                        } else {
                            imageFolderList.add(imageFolder);
                        }
                    }
                    observableEmitter.onNext(new RxOptional<>(imageFolderList));
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }


    public static List<SDFile> getImageList(SDFolder parentFolder) {
        final List<SDFile> sdFileList = new ArrayList<SDFile>();
        File dirFile = new File(parentFolder.getPath());
        if (dirFile != null && dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg");
                }
            });
            for (File file : files) {
                if (file.length() == 0) {
                    continue;
                }
                SDFile sdFile = new SDFile();
                sdFile.setFileType(FileChooser.FileType.TYPE_IMAGE);
                sdFile.setName(file.getName());
                sdFile.setPath(file.getPath());
                sdFile.setThumbUri(Uri.parse("file://" + file.getPath()));
                sdFile.setModifyDate(DateUtils.dateToStr(new Date(file.lastModified()), DateUtils.sYMDHMFormat.get()));
                sdFile.setSize(FileUtils.getFileSizeStr(file.length()));
                sdFileList.add(sdFile);
            }
        }
        Collections.sort(sdFileList, new Comparator<SDFile>() {
            @Override
            public int compare(SDFile lhs, SDFile rhs) {
                if (TextUtils.isEmpty(lhs.getModifyDate()) || TextUtils.isEmpty(rhs.getModifyDate())) {
                    return 0;
                }
                return rhs.getModifyDate().compareTo(lhs.getModifyDate());
            }
        });
        return sdFileList;
    }

    /**
     * 获取手机系统库里的所有视频目录
     *
     * @return
     */
    public static List<SDFolder> getAllVideoFolder() {
        List<SDFolder> folderList = new ArrayList<SDFolder>();

        String[] mediaColumns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };
        ContentResolver contentResolver = BaseApplication.getContext().getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
            if (cursor == null) {
                return folderList;
            }

            if (cursor.moveToFirst()) {
                List<SDFile> fileList = new ArrayList<SDFile>();
                Map<String, SDFolder> folderMap = new HashMap<String, SDFolder>();
                do {
                    try {
                        SDFile sdFile = new SDFile();
                        sdFile.setFileType(FileChooser.FileType.TYPE_VIDEO);

                        //视频路径
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        sdFile.setPath(path);

                        //视频时长
                        long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        sdFile.setSize(DateUtils.getMinuteAndSecond(duration));

                        //视频缩略图
                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                        String thumbUrl = getVideoThumbnail(BaseApplication.getContext(), id);
                        sdFile.setThumbUri(Uri.parse("file://" + thumbUrl));

                        //视频标题
//				sdFile.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

                        //视频名称
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        sdFile.setName(name);

                        //视频类型
//				sdFile.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));

                        fileList.add(sdFile);

                        SDFolder sdFolder = null;
                        //视频库路径
                        int index = TextUtils.isEmpty(path) ? -1 : path.lastIndexOf("/");
                        if (index == -1) {
                            continue;
                        }
                        String dir = path.substring(0, index);
                        if (folderMap.containsKey(dir)) {
                            sdFolder = folderMap.get(dir);
                        } else {
                            sdFolder = new SDFolder();
                            sdFolder.setFolderType(FileChooser.FileType.TYPE_VIDEO);
                            sdFolder.setPath(dir);
                            sdFolder.setImageUri(sdFile.getThumbUri());
                            if ("Camera".equalsIgnoreCase(sdFolder.getName())) {
//                                sdFolder.setName(BaseApplication.getContext().getString(R.string.camera));
                                folderList.add(0, sdFolder);
                            } else {
                                folderList.add(sdFolder);
                            }
                            folderMap.put(dir, sdFolder);
                        }
                        if (sdFolder.getFileList() == null) {
                            sdFolder.setFileList(new ArrayList<SDFile>());
                        }
                        sdFolder.getFileList().add(sdFile);
                        sdFolder.setCount(sdFolder.getFileList().size());
                    } catch (Exception e) {
                        continue;
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return folderList;
    }

    /**
     * 获取视频缩略图目录
     *
     * @param context
     * @param id
     * @return
     */

    private static String getVideoThumbnail(Context context, int id) {
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};

        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Video.Thumbnails.MICRO_KIND, null);

        Cursor thumbCursor = null;
        try {

            thumbCursor = context.getContentResolver().query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns,
                    MediaStore.Video.Thumbnails.VIDEO_ID + " = " + id, null, null);

            if (thumbCursor.moveToFirst()) {
                String thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));

                return thumbPath;
            }

        } finally {
            if (thumbCursor != null) {
                thumbCursor.close();
            }
        }

        return null;
    }

    /**
     * 获取所有文件
     *
     * @param parentFolder
     * @return
     */
    public static List[] getAllFile(SDFolder parentFolder) {
        if (!FileUtils.isExistExternalStore()) {
            return null;
        }
        List[] results = new List[2];
        File dirFile = new File(parentFolder.getPath());
        if (dirFile != null && dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    List<SDFolder> folderList = null;
                    if (results[0] == null) {
                        folderList = new ArrayList<SDFolder>();
                        results[0] = folderList;
                    }
                    SDFolder sdFolder = new SDFolder();
                    sdFolder.setParentFolder(parentFolder);
                    sdFolder.setPath(file.getPath());
                    results[0].add(sdFolder);
                } else {
                    if (file.length() == 0) {
                        continue;
                    }
                    List<SDFile> fileList = null;
                    if (results[1] == null) {
                        fileList = new ArrayList<SDFile>();
                        results[1] = fileList;
                    }
                    SDFile sdFile = new SDFile();
                    sdFile.setParentFolder(parentFolder);
                    sdFile.setFileType(FileChooser.FileType.TYPE_IMAGE);
                    sdFile.setName(file.getName());
                    sdFile.setPath(file.getPath());
                    sdFile.setModifyDate(DateUtils.dateToStr(new Date(file.lastModified()), DateUtils.sYMDHMFormat.get()));
                    sdFile.setSize(FileUtils.getFileSizeStr(file.length()));
                    results[1].add(sdFile);
                }
            }
        }
        if (results[0] != null) {
            Collections.sort(results[0]);
        }
        if (results[1] != null) {
            Collections.sort(results[1]);
        }
        return results;
    }

    /**
     * 获取所有文档
     *
     * @param path
     * @return
     */
    public static List<SDFile> getAllDocumentFile(String path) {
        List<SDFile> fileList = new ArrayList<SDFile>();
        getDocumentInPath(path, fileList);
        Collections.sort(fileList, new Comparator<SDFile>() {
            @Override
            public int compare(SDFile lhs, SDFile rhs) {
                if (lhs.getModifyDate() == null) {
                    return -1;
                }
                if (rhs.getModifyDate() == null) {
                    return 1;
                }
                return lhs.getModifyDate().compareTo(rhs.getModifyDate());
            }
        });
        return fileList;
    }

    /**
     * 获取指定目录下所有文档
     *
     * @param path
     * @param list
     */
    public static void getDocumentInPath(String path, List<SDFile> list) {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    String filename = file.getName();
                    return file.isDirectory()
                            || (filename.toLowerCase().endsWith(".txt")
                            || filename.toLowerCase().endsWith(".doc")
                            || filename.toLowerCase().endsWith(".docx")
                            || filename.toLowerCase().endsWith(".pdf")
                            || filename.toLowerCase().endsWith(".ppt")
                            || filename.toLowerCase().endsWith(".pptx")
                            || filename.toLowerCase().endsWith(".xlsx")
                            || filename.toLowerCase().endsWith(".htm")
                            || filename.toLowerCase().endsWith(".html")
//							|| filename.toLowerCase().endsWith(".wpd")
//							|| filename.toLowerCase().endsWith(".log")
//							|| filename.toLowerCase().endsWith(".rtf")
                    );
                }
            });
            for (File file : files) {
                if (file.isDirectory()) {
                    getDocumentInPath(file.getPath(), list);
                } else {
                    SDFile sdFile = new SDFile();
                    sdFile.setName(file.getName());
                    sdFile.setPath(file.getPath());
                    sdFile.setModifyDate(DateUtils.dateToStr(new Date(file.lastModified()), DateUtils.sYMDHMFormat.get()));
                    sdFile.setSize(FileUtils.getFileSizeStr(file.length()));
                    sdFile.setFileType(FileChooser.FileType.TYPE_DOCUMENT);
                    list.add(sdFile);
                }
            }
        }
    }

    /**
     * 获取手机里所有音乐文件
     *
     * @return
     */
    public static List<SDFile> getAllMusic() {
        List<SDFile> fileList = new ArrayList<SDFile>();
        ContentResolver cr = BaseApplication.getContext().getContentResolver();
        // 获取所有歌曲
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        try {
            if (null == cursor) {
                return null;
            }
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//					int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    SDFile file = new SDFile();
                    file.setName(name);
                    file.setPath(path);
                    file.setModifyDate(DateUtils.dateToStr(new Date(time * 1000), DateUtils.sYMDHMFormat.get()));
                    file.setSize(FileUtils.getFileSizeStr(size));
                    file.setFileType(FileChooser.FileType.TYPE_MUSIC);
                    fileList.add(file);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileList;
    }
}
