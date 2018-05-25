package com.baigu.dms.common.view.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.imagepicker.adapter.ImagePagerAdapter;
import com.baigu.dms.common.view.imagepicker.model.GalleryImageBean;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerPreviewActivity extends BaseActivity implements ImagePagerAdapter.ImagePagerListener, View.OnClickListener, ViewPager.OnPageChangeListener, ImagePreviewActionProvider.PreviewActionClickListener {

    private ViewPager mViewPager;
    private CheckBox mCbImageOrigin;
    private TextView mTvImageSize;
    private TextView mTvSend;

    private SDFolder mSDFolder;
    private SDFile mSDFile;
    private GalleryImageBean mImageBean;
    private List<SDFile> mSelectedList;

    private  ImagePreviewActionProvider mPreviewActionProvider;

    private ImagePagerAdapter<GalleryImageBean> mImagePagerAdapter;

    /**上次已选择的图片数量*/
    private int mPreSelectedCount;

    /**图片选择器配置选项*/
    private ImagePickerActivity.ImagePickerOptions mImagePickerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_preview);

        initToolBar();

        mSDFolder = getIntent().getParcelableExtra("folder");
        mSDFile = getIntent().getParcelableExtra("file");
        mSelectedList = getIntent().getParcelableArrayListExtra(ImagePickerActivity.FLAG_SELECTED_IMAGE_LIST);

        mImagePickerOptions = getIntent().getParcelableExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS);
        if (mImagePickerOptions == null) {
            mImagePickerOptions = new ImagePickerActivity.ImagePickerOptions();
        }

        mPreSelectedCount = getIntent().getIntExtra(ImagePickerActivity.FLAG_PRE_SELECTED_COUNT, 0);

        initView();

        mCbImageOrigin.setVisibility(mImagePickerOptions.isEnableOriginImage() ? View.VISIBLE : View.GONE);

        if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
            mTvSend.setText(mImagePickerOptions.getButtonText());
        }

        //将数据封装成Adapter能使用的数据
        if (mSDFolder == null || ImagePickerActivity.sFileList == null || ImagePickerActivity.sFileList.size() == 0 || mSDFile == null) {
            finish();
            return;
        }

        mSDFolder.setFileList(ImagePickerActivity.sFileList);

        List<String> urlList = new ArrayList<String>();
        int selectedPos = mSDFolder.getFileList().indexOf(mSDFile);
        int count = 0;
        for (SDFile file : mSDFolder.getFileList()) {
            if (file.getFileType() == ImagePickerActivity.FLAG_CAMERA) {
                continue;
            }
            if (mSDFile.equals(file)) {
                selectedPos = count;
            }
            count++;
            urlList.add(file.getPath());
        }

        mImageBean = new GalleryImageBean();
        mImageBean.setUrlList(urlList);
        mImagePagerAdapter.setData(mImageBean);
        mImagePagerAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(selectedPos);

        updateSelectedInfo(selectedPos);
        onSelectedItemChanged();
    }

    private void initView() {
        findViewById(R.id.layout_image_origin).setOnClickListener(this);
        mCbImageOrigin = findView(R.id.cb_image_origin);
        mTvImageSize = findView(R.id.tv_image_size);

        mTvSend = findView(R.id.tv_send);
        mTvSend.setOnClickListener(this);

        mViewPager = findView(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mImagePagerAdapter = new ImagePagerAdapter<GalleryImageBean>(this);
        mImagePagerAdapter.setImagePagerListener(this);
        mViewPager.setAdapter(mImagePagerAdapter);
    }

    private void updateSelectedInfo(int position) {
        setTitle(getString(R.string.image_preview_num, position + 1, mImageBean.getUrlList().size()));
        if (mSDFolder.getFileList() == null || mSDFolder.getFileList().size() <= 0) {
            return;
        }
        int fileIndex = mSDFolder.getFileList().get(0).getFileType() == ImagePickerActivity.FLAG_CAMERA ? position + 1 : position;
        SDFile sdFile = mSDFolder.getFileList().get(fileIndex);
        mTvImageSize.setText(getString(R.string.origin_image_size, sdFile.getSize()));
        if (mPreviewActionProvider != null)
        mPreviewActionProvider.setPreviewActionSelect(mSelectedList.contains(sdFile));
    }

    private void onSelectedItemChanged() {
        if (mSelectedList != null && mSelectedList.size() > 0) {
            if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
                String text = StringUtils.getImageSelectButtonText(mImagePickerOptions.getButtonText(), mSelectedList.size(), (mImagePickerOptions.getMaxSelect() - mPreSelectedCount));
                mTvSend.setText(text);
            } else {
                mTvSend.setText(getString(R.string.send_num, mSelectedList.size(), mImagePickerOptions.getMaxSelect() - mPreSelectedCount));
            }
            mTvSend.setTextColor(getResources().getColor(R.color.white));
            mTvSend.setBackgroundResource(R.drawable.im_btn_img_send_num);
        } else {
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
    protected void onDestroy() {
        super.onDestroy();
        if (mSDFolder != null) {
            mSDFolder = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateSelectedInfo(mViewPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_picker_preview, menu);
        MenuItem menuItem = menu.findItem(R.id.action_select);
        mPreviewActionProvider  = (ImagePreviewActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mPreviewActionProvider.setPreviewActionClickListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_preview) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onBackClick(View v) {
        Intent intent = getIntent();
        intent.putExtra(ImagePickerActivity.FLAG_SELECTED_IMAGE_LIST, (ArrayList<SDFile>) mSelectedList);
        intent.removeExtra("folder"); //数据量太大
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onPreviewActionSelected(boolean b) {
        int fileIndex = mSDFolder.getFileList().get(0).getFileType() == ImagePickerActivity.FLAG_CAMERA ? mViewPager.getCurrentItem() + 1 : mViewPager.getCurrentItem();
        SDFile sdFile = mSDFolder.getFileList().get(fileIndex);
        if (b) {
            mSelectedList.remove(sdFile);
        } else {
            if (!mSelectedList.contains(sdFile)) {
                int maxSelect = mImagePickerOptions.getMaxSelect() - mPreSelectedCount;
                if (mSelectedList.size() >= maxSelect) {
                    ViewUtils.showToastInfo(BaseApplication.getContext().getString(R.string.over_max_image_select, maxSelect));
                    return false;
                }
                mSelectedList.add(sdFile);
            }
        }
        onSelectedItemChanged();
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        updateSelectedInfo(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_image_origin) {
            mCbImageOrigin.setChecked(!mCbImageOrigin.isChecked());
        } else if (v.getId() == R.id.tv_send) {
            if (mSelectedList == null || mSelectedList.size() <= 0) {
                return;
            }
            Intent intent = getIntent();
            intent.removeExtra("folder"); //数据量太大
            intent.putExtra(ImagePickerActivity.FLAG_SELECTED_IMAGE_LIST, (ArrayList<SDFile>) mSelectedList);
            intent.putExtra(ImagePickerActivity.FLAG_ORIGIN_IMAGE, mCbImageOrigin.isChecked());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onImageClick(View v) {

    }

    @Override
    public void onLongCLicked(String url) {

    }

    @Override
    public void onStartLoadImage() {

    }

    @Override
    public void onLoadedImage(boolean isSuccess) {

    }
}
