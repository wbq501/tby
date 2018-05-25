package com.baigu.dms.common.view.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.baigu.dms.common.utils.rxpermission.RxPermissions;
import com.baigu.dms.common.view.imagepicker.adapter.ImageAdapter;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.micky.logger.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ImageChooserActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView mGvImage;
    private ProgressBar mProgressBar;
    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_chooser);

        initToolBar();

        final SDFolder imageFolder = getIntent().getParcelableExtra("folder");

        int titleResId = imageFolder.getFolderType() == FileChooser.FileType.TYPE_IMAGE ? R.string.select_img_folder : R.string.select_video;
        setTitle(titleResId);

        initView();

        String appName = getString(R.string.app_name);
        String tip = getString(R.string.permission_sd_camera, appName, appName);
        PermissionRequest permissionRequest = new PermissionRequest(this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
            @Override
            public void onGrant() {
                if (FileUtils.isExistExternalStore()) {
                    if (imageFolder.getFolderType() == FileChooser.FileType.TYPE_IMAGE) {
                        loadImageList(imageFolder);
                    } else if (imageFolder.getFolderType() == FileChooser.FileType.TYPE_VIDEO) {
                        mImageAdapter.setData(imageFolder.getFileList());
                        mImageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        permissionRequest.requestPermission(true);
    }

    private void initView() {
        mGvImage = findView(R.id.gv_image);
        int paddingLeft = ViewUtils.dip2px(5);
        int horizontalSpacing = ViewUtils.dip2px(5);
        mGvImage.setPadding(paddingLeft, 0, paddingLeft, 0);
        mGvImage.setHorizontalSpacing(horizontalSpacing);
        mGvImage.setVerticalSpacing(horizontalSpacing);
        mGvImage.setOnItemClickListener(this);

        mImageAdapter = new ImageAdapter(this);
        int columnWidth = (int) ((ViewUtils.getScreenInfo(this).widthPixels - paddingLeft * 2 - horizontalSpacing * 3) * 0.25);
        mImageAdapter.setColumnSize(columnWidth, columnWidth);
        mGvImage.setAdapter(mImageAdapter);

        mProgressBar = findView(R.id.progress_bar);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SDFile sdFile = mImageAdapter.getItem(position);
        Intent intent = getIntent();
        intent.putExtra("path", sdFile.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void loadImageList(final SDFolder imageFolder) {
        if (!FileUtils.isExistExternalStore()) {
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                File dirFile = new File(imageFolder.getPath());
                if (dirFile != null && dirFile.exists() && dirFile.isDirectory()) {
                    final List<SDFile> sdFileList = new ArrayList<SDFile>();
                    File[] files = dirFile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg");
                        }
                    });
                    for (File file : files) {
                        SDFile sdFile = new SDFile();
                        sdFile.setFileType(FileChooser.FileType.TYPE_IMAGE);
                        sdFile.setName(file.getName());
                        sdFile.setPath(file.getPath());
                        sdFile.setThumbUri(Uri.parse("file://" + file.getPath()));
                        sdFile.setModifyDate(DateUtils.dateToStr(new Date(file.lastModified()), DateUtils.sYMDHMFormat.get()));
                        sdFile.setSize(FileUtils.getFileSizeStr(file.length()));
                        sdFileList.add(sdFile);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mImageAdapter.setData(sdFileList);
                            mImageAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
