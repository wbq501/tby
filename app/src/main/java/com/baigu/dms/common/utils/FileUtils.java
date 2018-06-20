package com.baigu.dms.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.micky.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/5 22:38
 */
public class FileUtils {

    public static final String BASE_FILE_PATH = getExternalStorePath() + File.separator + "DMS";

    public static final String APK_LOCAL_PATH = BASE_FILE_PATH + File.separator + "apk";

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建文件
     *
     * @param folderPath
     * @param fileName
     * @return
     */
    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }


    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public static File createDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
    }

    public static File createOutputDCIMFile() {
        String timestamp = DateUtils.dateToStr(new Date(System.currentTimeMillis()), DateUtils.sYMD_HMSFormat.get());
        String fileName = "IMG_" + timestamp + ".jpg";
        return createFile(BASE_FILE_PATH + File.separator + "images", fileName);
    }

    /**
     * 创建裁剪图
     */
    public static File createOutputCropFile() {
        String timestamp = DateUtils.dateToStr(new Date(System.currentTimeMillis()), DateUtils.sYMD_HMSFormat.get());
        String fileName = "IMG_" + timestamp + ".jpg";
        return createFile(BASE_FILE_PATH + File.separator + "crop", fileName);
    }

    /**
     * 删除裁剪图
     */
    public static boolean deleteOutputCropDir() {
        return deleteDir(BASE_FILE_PATH + File.separator + "crop");
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    public static boolean deleteDir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            return deleteDir(file);
        }
        return true;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件(不删除目录）
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    public static boolean deleteFileInDir(File dir) {
        try {
            if (dir.exists() && dir.isDirectory()) {
                File[] fileList = dir.listFiles();
                for (File file : fileList) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        deleteFileInDir(file);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(File srcFile, File destFile, boolean deleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (deleteSrc) {
                srcFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 获取文件大小显示的字符串
     *
     * @param fileSize
     * @return
     */
    public static String getFileSizeStr(long fileSize) {
        String result = "";
        DecimalFormat df = new DecimalFormat("#.#");
        if (fileSize < 1024) {
            result = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            result = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            result = df.format((double) fileSize / 1048576) + "MB";
        } else {
            result = df.format((double) fileSize / 1073741824) + "GB";
        }
        if (result.indexOf(".") > 0) {
            result = result.replaceAll("0+?$", "");//去掉后面无用的零
            result = result.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return result;
    }

    public static void savePhoto(final Context context, final Bitmap bmp, final SaveResultCallback saveResultCallback) {
        final File sdDir = getSDPath();
        if (sdDir == null) {
            Toast.makeText(context,"设备自带的存储不可用", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(sdDir, "tby_photo");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置以当前时间格式为图片名称
                String fileName = df.format(new Date()) + ".png";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess();
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }

    public interface SaveResultCallback {
        void onSavedSuccess();

        void onSavedFailed();
    }

}
