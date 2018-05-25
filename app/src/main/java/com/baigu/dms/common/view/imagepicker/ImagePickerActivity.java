package com.baigu.dms.common.view.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.baigu.dms.common.utils.rxpermission.RxPermissions;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.imagepicker.adapter.ImagePickerAdapter;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.baigu.dms.domain.file.FileRepository;
import com.micky.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnImagePickerListener,
        ImagePickerFolderView.ImagePickerFolderViewListener, OnRVItemClickListener {

    public static final int FLAG_ALL_IMAGE_FOLDER = 1111;
    public static final int FLAG_CAMERA = 1112;

    public static final int REQUEST_IMAGE_PICKER = 2111;

    /**
     * 用于intent传数据，表示已选中列表
     */
    public static final String FLAG_SELECTED_IMAGE_LIST = "selectedList";
    /**
     * 用于intent传数据，表示是否是原图
     */
    public static final String FLAG_ORIGIN_IMAGE = "originImage";
    /**
     * 用于intent传数据，选中的文件路径
     */
    public static final String FLAG_SELECTED_PATH_LIST = "pathList";

    /**
     * 用于intent传数据，已提前选中的图片数
     */
    public static final String FLAG_PRE_SELECTED_COUNT = "preSelectedImageCount";

    /**
     * 用于intent传数据，图片选择器配置选项
     */
    public static final String FLAG_IMAGE_PICKER_OPTIONS = "image_picker_options";

    public static List<SDFile> sFileList = null; //数据量太大，不能使用intent传输


    private RecyclerView mRvImage;
    private TextView mTvFolder;
    private CheckBox mCbFolder;
    private TextView mTvSend;
    private MenuItem mRightMenuItem;
    private View mLayoutBottomBar;
    private ImagePickerFolderView mPickerFolderView;

    private ProgressBar mProgressBar;
    private ImagePickerAdapter mImagePickerAdapter;
    private SDFolder mSelectedFolder;

    private TakePictureUtils mTakePictureUtils;

    /**
     * 上次已选择的图片数量
     */
    private int mPreSelectedCount;

    /**
     * 图片选择器配置选项
     */
    private ImagePickerOptions mImagePickerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        initToolBar();

        setTitle(R.string.select_img);

        initView();

        mImagePickerOptions = getIntent().getParcelableExtra(FLAG_IMAGE_PICKER_OPTIONS);
        if (mImagePickerOptions == null) {
            mImagePickerOptions = new ImagePickerOptions();
        }

        if (!mImagePickerOptions.isEnableMultiSelect()) {
            mTvSend.setVisibility(View.GONE);
            mImagePickerAdapter.setEnableMultiSelect(false);
        } else if (mImagePickerOptions.getMaxSelect() != 0) {
            mImagePickerAdapter.setMaxSelect(mImagePickerOptions.getMaxSelect());
        }
        if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
            mTvSend.setText(mImagePickerOptions.getButtonText());
        }

        List<String> selectedFileList = mImagePickerOptions.getSelectedFileList();
        if (selectedFileList != null && selectedFileList.size() > 0) {
            mPreSelectedCount = selectedFileList.size();
            mImagePickerAdapter.setPreSelectedCount(mPreSelectedCount);
        }

        String appName = getString(R.string.app_name);
        String tip = getString(R.string.permission_camera, appName, appName);
        PermissionRequest permissionRequest = new PermissionRequest(this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
            @Override
            public void onGrant() {
                if (FileUtils.isExistExternalStore()) {
                    loadData();
                }
            }
        });
        permissionRequest.requestPermission(true);
    }

    private void initView() {

        mTvFolder = findView(R.id.tv_folder);
        mCbFolder = findView(R.id.cb_folder);
        mTvSend = findView(R.id.tv_send);
        mTvSend.setOnClickListener(this);

        findViewById(R.id.layout_show_folder).setOnClickListener(this);
        mLayoutBottomBar = findViewById(R.id.layout_bottom_bar);

        mRvImage = findView(R.id.rv_image);
        int padding = ViewUtils.dip2px(2);
        mRvImage.setPadding(padding, 0, padding, 0);
        mRvImage.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL);
        mRvImage.setLayoutManager(gridLayoutManager);
        mRvImage.addItemDecoration(new ImagePickerItemDecoration(padding));

        mImagePickerAdapter = new ImagePickerAdapter();
        mImagePickerAdapter.setOnImagePickerListener(this);
        mImagePickerAdapter.setOnRVItemClickListener(this);
        int columnWidth = (int) ((ViewUtils.getScreenInfo(this).widthPixels - padding * 2) * 0.25);
        mImagePickerAdapter.setColumnSize(columnWidth, columnWidth);
        mRvImage.setAdapter(mImagePickerAdapter);

        mProgressBar = findView(R.id.progress_bar);
        mPickerFolderView = new ImagePickerFolderView(this);
        mPickerFolderView.setImagePickerFolderViewListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_picker, menu);
        mRightMenuItem = menu.findItem(R.id.action_preview);
        mRightMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_preview) {
            Intent intent = new Intent(this, ImagePickerSelPreviewActivity.class);
            intent.putExtra(FLAG_IMAGE_PICKER_OPTIONS, mImagePickerOptions);
            intent.putExtra(FLAG_PRE_SELECTED_COUNT, mPreSelectedCount);
            intent.putExtra(FLAG_SELECTED_IMAGE_LIST, (ArrayList<SDFile>) mImagePickerAdapter.getSelectedList());
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER) { //相片选择
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    List<SDFile> fileList = data.getParcelableArrayListExtra(FLAG_SELECTED_IMAGE_LIST);
                    ArrayList<String> pathList = new ArrayList<String>();
                    for (SDFile sdFile : fileList) {
                        pathList.add(sdFile.getPath());
                    }
                    Intent intent = getIntent();
                    intent.putStringArrayListExtra(FLAG_SELECTED_PATH_LIST, pathList);
                    intent.putExtra(ImagePickerActivity.FLAG_ORIGIN_IMAGE, data.getBooleanExtra(ImagePickerActivity.FLAG_ORIGIN_IMAGE, false));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    List<SDFile> fileList = data.getParcelableArrayListExtra(FLAG_SELECTED_IMAGE_LIST);
                    mImagePickerAdapter.setData(mSelectedFolder.getFileList());
                    mImagePickerAdapter.setSelectedList(fileList);
                    mImagePickerAdapter.notifyDataSetChanged();
                    onSelectedItemChanged(fileList);
                }
            }
        } else if (requestCode == TakePictureUtils.REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                mTakePictureUtils.onActivityResult(requestCode, resultCode, data, new TakePictureUtils.PictureListener() {
                    @Override
                    public void onPictureTake(String path) {

                        if (!mImagePickerOptions.isEnableMultiSelect()) {
                            Intent intent = getIntent();
                            ArrayList<String> pathList = new ArrayList<String>();
                            pathList.add(path);
                            intent.putStringArrayListExtra(FLAG_SELECTED_PATH_LIST, pathList);
                            setResult(RESULT_OK, intent);
                            finish();
                            return;
                        }
                        if (TextUtils.isEmpty(path)) {
                            return;
                        }
                        File file = new File(path);
                        if (!file.exists()) {
                            return;
                        }
                        SDFile sdFile = new SDFile();
                        sdFile.setFileType(FileChooser.FileType.TYPE_IMAGE);
                        sdFile.setPath(file.getPath());
                        sdFile.setName(file.getName());
                        sdFile.setSize(FileUtils.getFileSizeStr(file.length()));
                        ArrayList<SDFile> fileList = new ArrayList<SDFile>();
                        fileList.add(sdFile);
                        Intent intent = new Intent(ImagePickerActivity.this, ImagePickerSelPreviewActivity.class);
                        intent.putExtra(ImagePickerSelPreviewActivity.FLAG_SINGLE_PREVIEW, true);
                        intent.putExtra(FLAG_IMAGE_PICKER_OPTIONS, mImagePickerOptions);
                        intent.putExtra(FLAG_SELECTED_IMAGE_LIST, fileList);
                        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
                    }
                });
            }
        }
    }


    @Override
    public void finish() {
        sFileList = null;
        super.finish();

    }

    @Override
    protected void onDestroy() {
        sFileList = null;
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        SDFile sdFile = (SDFile) adapter.getItem(position);
        if (!mImagePickerOptions.isEnableMultiSelect()) {
            Intent intent = getIntent();
            ArrayList<String> pathList = new ArrayList<String>();
            pathList.add(sdFile.getPath());
            intent.putStringArrayListExtra(FLAG_SELECTED_PATH_LIST, pathList);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        Intent intent = new Intent(this, ImagePickerPreviewActivity.class);
        if (mSelectedFolder == null) return;
        //数据量太大，Inent不能传输，所以使用静态变量赋值方式
        sFileList = mSelectedFolder.getFileList();
        SDFolder sdFolder = new SDFolder();
        sdFolder.setName(mSelectedFolder.getName());
        sdFolder.setCount(mSelectedFolder.getCount());
        sdFolder.setFolderType(mSelectedFolder.getFolderType());
        sdFolder.setImageUri(mSelectedFolder.getImageUri());
        sdFolder.setPath(mSelectedFolder.getPath());
        intent.putExtra("folder", sdFolder);
        intent.putExtra("file", sdFile);
        intent.putExtra(FLAG_IMAGE_PICKER_OPTIONS, mImagePickerOptions);
        intent.putExtra(FLAG_PRE_SELECTED_COUNT, mPreSelectedCount);
        intent.putExtra(FLAG_SELECTED_IMAGE_LIST, (ArrayList<SDFile>) mImagePickerAdapter.getSelectedList());
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    @Override
    public void onCameraClick() {
        mTakePictureUtils = new TakePictureUtils(ImagePickerActivity.this, false);
        mTakePictureUtils.takeFromCamera();
    }

    @Override
    public void onFolderSelected(SDFolder folder) {
        mSelectedFolder = folder;
        mTvFolder.setText(mSelectedFolder.getName());
        mImagePickerAdapter.setData(folder.getFileList());
        mImagePickerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedItemChanged(List<SDFile> list) {
        if (list != null && list.size() > 0) {
            mRightMenuItem.setVisible(true);
            mRightMenuItem.setTitle(getString(R.string.preview_num, list.size()));
            if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
                String text = StringUtils.getImageSelectButtonText(mImagePickerOptions.getButtonText(), list.size(), (mImagePickerOptions.getMaxSelect() - mPreSelectedCount));
                mTvSend.setText(text);
            } else {
                mTvSend.setText(getString(R.string.send_num, list.size(), mImagePickerOptions.getMaxSelect() - mPreSelectedCount));
            }
            mTvSend.setTextColor(getResources().getColor(R.color.white));
            mTvSend.setBackgroundResource(R.drawable.im_btn_img_send_num);
        } else {
            mRightMenuItem.setVisible(false);

            if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
                mTvSend.setText(mImagePickerOptions.getButtonText());
            } else {
                mTvSend.setText(R.string.send);
            }
            mTvSend.setTextColor(getResources().getColor(R.color.color_999999));
            mTvSend.setBackgroundResource(R.drawable.im_btn_img_send);
        }
    }

    @Override
    public void onFolderViewShowed() {
        mCbFolder.setChecked(true);
    }

    @Override
    public void onFolderViewDismissed() {
        mCbFolder.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        if (v.getId() == R.id.tv_send) {
            if (mImagePickerAdapter.getSelectedList() == null || mImagePickerAdapter.getSelectedList().size() <= 0) {
                return;
            }
            List<String> pathList = new ArrayList<String>();
            for (SDFile sdFile : mImagePickerAdapter.getSelectedList()) {
                pathList.add(sdFile.getPath());
            }
            Intent intent = getIntent();
            intent.putStringArrayListExtra(FLAG_SELECTED_PATH_LIST, (ArrayList<String>) pathList);
            intent.putExtra(ImagePickerActivity.FLAG_ORIGIN_IMAGE, false);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.layout_show_folder) {
            if (!mPickerFolderView.isShowing()) {
                int[] location = new int[2];
                mLayoutBottomBar.getLocationOnScreen(location);
                mPickerFolderView.showAtLocation(mLayoutBottomBar, Gravity.NO_GRAVITY, 0, location[1] - mPickerFolderView.getHeight());
            } else {
                mPickerFolderView.dismiss();
            }

        }
    }


    private void loadData() {
        if (!FileUtils.isExistExternalStore()) {
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        FileRepository.getImageFolderAndImages()
                .subscribeOn(Schedulers.io())
                .map(new Function<RxOptional<List<SDFolder>>, RxOptional<List<SDFolder>>>() {
                    @Override
                    public RxOptional<List<SDFolder>> apply(@NonNull RxOptional<List<SDFolder>> rxOptional) throws Exception {
                        List<SDFile> allImageList = new ArrayList<SDFile>();
                        if (rxOptional.isEmpty()) {
                            return rxOptional;
                        }
                        for (SDFolder folder : rxOptional.get()) {
                            if (folder.getFileList() != null && folder.getFileList().size() > 0) {
                                allImageList.addAll(folder.getFileList());
                            }
                            if (mImagePickerOptions.isShowCamera()) {
                                //为每个folder构造一个相机SDFile用于显示相机图标
                                SDFile cameraFile = new SDFile();
                                cameraFile.setFileType(FLAG_CAMERA);
                                folder.getFileList().add(0, cameraFile);
                            }
                        }
                        Collections.sort(allImageList, new Comparator<SDFile>() {
                            @Override
                            public int compare(SDFile lhs, SDFile rhs) {
                                if (TextUtils.isEmpty(lhs.getModifyDate()) || TextUtils.isEmpty(rhs.getModifyDate())) {
                                    return 0;
                                }
                                return rhs.getModifyDate().compareTo(lhs.getModifyDate());
                            }
                        });

                        if (mImagePickerOptions.isShowCamera()) {
                            //为“所有图片folder”构造一个相机SDFile用于显示相机图标
                            SDFile cameraFile = new SDFile();
                            cameraFile.setFileType(FLAG_CAMERA);
                            allImageList.add(0, cameraFile);
                        }

                        //构造一个包含所有图片的Folder
                        SDFolder allImageFolder = new SDFolder();
                        allImageFolder.setFolderType(FLAG_ALL_IMAGE_FOLDER);
                        if (allImageList.size() > 1) {
                            allImageFolder.setImageUri(allImageList.get(1).getThumbUri());
                            allImageFolder.setCount(allImageList.size() - 1);
                        } else {
                            allImageFolder.setCount(0);
                        }
                        allImageFolder.setPath("###all_image_path***");
                        allImageFolder.setFileList(allImageList);
                        allImageFolder.setName(getString(R.string.all_image));

                        rxOptional.get().add(0, allImageFolder);
                        return rxOptional;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RxOptional<List<SDFolder>>>() {
                    @Override
                    public void accept(RxOptional<List<SDFolder>> optional) throws Exception {
                        mProgressBar.setVisibility(View.GONE);
                        if (!optional.isEmpty() && optional.get().size() > 0) {
                            mImagePickerAdapter.setData(optional.get().get(0).getFileList());
                            mImagePickerAdapter.notifyDataSetChanged();
                            mPickerFolderView.setData(optional.get());
                            mSelectedFolder = optional.get().get(0);
                            mTvFolder.setText(mSelectedFolder.getName());
                            mPickerFolderView.setSelectedFolder(mSelectedFolder);
                        } else {
                            mImagePickerAdapter.setData(optional.get().get(0).getFileList());
                            mImagePickerAdapter.notifyDataSetChanged();
                            mPickerFolderView.setData(null);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 图片选择器配置选项，默认多选，显示相机
     */
    public static class ImagePickerOptions implements Parcelable {
        /**
         * 是否支持多选
         */
        private boolean enableMultiSelect = true;

        /**
         * 是否显示相机
         */
        private boolean showCamera = true;

        /**
         * 确定按钮文字
         */
        private String buttonText;

        /**
         * 最大选择数,与enableMultiSelect有关联
         */
        private int maxSelect = Constants.IMAGE_PICKER_MAX_SELECT;

        /**
         * 是否能选择原图发送
         */
        private boolean enableOriginImage = false;

        /**
         * 选中的文件路径列表
         */
        private List<String> selectedFileList;

        public boolean isEnableMultiSelect() {
            return enableMultiSelect;
        }

        public void setEnableMultiSelect(boolean enableMultiSelect) {
            this.enableMultiSelect = enableMultiSelect;
            if (!enableMultiSelect) {
                this.maxSelect = 1;
            }
        }

        public boolean isShowCamera() {
            return showCamera;
        }

        public void setShowCamera(boolean showCamera) {
            this.showCamera = showCamera;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }

        public int getMaxSelect() {
            return maxSelect;
        }

        public void setMaxSelect(int maxSelect) {
            this.maxSelect = maxSelect;
            if (maxSelect <= 1) {
                this.maxSelect = 1;
                this.enableMultiSelect = false;
            }
        }

        public boolean isEnableOriginImage() {
            return enableOriginImage;
        }

        public void setEnableOriginImage(boolean enableOriginImage) {
            this.enableOriginImage = enableOriginImage;
        }

        public List<String> getSelectedFileList() {
            return selectedFileList;
        }

        public void setSelectedFileList(List<String> selectedFileList) {
            this.selectedFileList = selectedFileList;
        }

        public ImagePickerOptions() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.enableMultiSelect ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showCamera ? (byte) 1 : (byte) 0);
            dest.writeString(this.buttonText);
            dest.writeInt(this.maxSelect);
            dest.writeByte(this.enableOriginImage ? (byte) 1 : (byte) 0);
            dest.writeStringList(this.selectedFileList);
        }

        protected ImagePickerOptions(Parcel in) {
            this.enableMultiSelect = in.readByte() != 0;
            this.showCamera = in.readByte() != 0;
            this.buttonText = in.readString();
            this.maxSelect = in.readInt();
            this.enableOriginImage = in.readByte() != 0;
            this.selectedFileList = in.createStringArrayList();
        }

        public static final Creator<ImagePickerOptions> CREATOR = new Creator<ImagePickerOptions>() {
            @Override
            public ImagePickerOptions createFromParcel(Parcel source) {
                return new ImagePickerOptions(source);
            }

            @Override
            public ImagePickerOptions[] newArray(int size) {
                return new ImagePickerOptions[size];
            }
        };
    }
}
