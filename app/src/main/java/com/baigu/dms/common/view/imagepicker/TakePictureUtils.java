package com.baigu.dms.common.view.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.webkit.PermissionRequest;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.rxpermission.RxPermissions;
import com.baigu.dms.fragment.BaseFragment;
import com.micky.logger.Logger;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.baigu.dms.R.style.PermissionDialog;

/**
 * Created by liqinglong on 16/7/12.
 */
public class TakePictureUtils {
    public static final int REQUEST_CAMERA = 1000;
    public static final int REQUEST_ALBUM = 1001;
    public static final int REQUEST_CROP = 1002;

    private String mOriginalPicturePath;
    private BaseActivity mBaseActivity;
    //图片宽度，临时存储
    private int mImageWidth;
    //图片高度，临时存储
    private int mImageHeight;

    private boolean mNeedToCrop;

    public interface PictureListener {
        void onPictureTake(String path);
}

    public TakePictureUtils(BaseActivity baseActivity) {
        this(baseActivity, false);
    }


    public TakePictureUtils(BaseActivity baseActivity, boolean needToCrop) {
        this.mNeedToCrop = needToCrop;
        mBaseActivity = baseActivity;
    }

    public void takeFromCamera() {

        String appName = mBaseActivity.getString(R.string.app_name);
        String tip = mBaseActivity.getString(R.string.permission_sd_camera, appName, appName);
        com.baigu.dms.common.utils.rxpermission.PermissionRequest permissionRequest = new com.baigu.dms.common.utils.rxpermission.PermissionRequest(mBaseActivity, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        permissionRequest.setPermissionListener(new com.baigu.dms.common.utils.rxpermission.PermissionRequest.PermissionListener() {
            @Override
            public void onGrant() {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(mBaseActivity.getPackageManager()) != null) {
                        intent.putExtra("width", 200);
                        File file = FileUtils.createOutputDCIMFile();
                        mOriginalPicturePath = file.getAbsolutePath();
                        Uri uri = parseUri(mBaseActivity, file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        if (mBaseActivity != null) {
                            mBaseActivity.startActivityForResult(intent, REQUEST_CAMERA);
                        }
                    }

                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
            }
        });
        permissionRequest.requestPermission(true);

    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parseUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

//    /**
//     * start to camera、preview、crop
//     */
//    public void startOpenCamera(Context context) {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
//            File cameraFile = PictureFileUtils.createCameraFile(this,
//                    mimeType == PictureConfig.TYPE_ALL ? PictureConfig.TYPE_IMAGE : mimeType);
//            cameraPath = cameraFile.getAbsolutePath();
//            Uri imageUri = parUri(cameraFile);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
//        }
//    }

    public void takeFromAlbum() {
        Intent intent = new Intent(mBaseActivity, ImageFolderChooserActivity.class);
        intent.putExtra("type", OperationType.FileOperationType.TYPE_IMAGE);
        mBaseActivity.startActivityForResult(intent, REQUEST_ALBUM);
    }

    public void takeFromImagePicker(ImagePickerActivity.ImagePickerOptions options) {
        Intent intent = new Intent(mBaseActivity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS, options);
        mBaseActivity.startActivityForResult(intent, ImagePickerActivity.REQUEST_IMAGE_PICKER);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent, PictureListener
            pictureListener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (mNeedToCrop) {
//                        goCrop(Uri.parse("file:" + mOriginalPicturePath));
                    } else {
                        if (pictureListener != null) {
                            pictureListener.onPictureTake(mOriginalPicturePath);
                        }
                    }
                    break;

                case REQUEST_ALBUM:
                    String path = intent.getStringExtra("path");
                    if (mNeedToCrop) {
//                        goCrop(Uri.parse("file:" + path));
                    } else {
                        if (pictureListener != null) {
                            pictureListener.onPictureTake(path);
                        }
                    }
                    break;
                case REQUEST_CROP:
                    if (pictureListener != null) {
                        pictureListener.onPictureTake(intent.getData().getPath());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public int getImageHeight() {
        return mImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        mImageHeight = imageHeight;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }
}
