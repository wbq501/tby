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

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerSelPreviewActivity extends BaseActivity implements ImagePagerAdapter.ImagePagerListener, View.OnClickListener, ViewPager.OnPageChangeListener, ImagePreviewActionProvider.PreviewActionClickListener {

    private ViewPager mViewPager;
    private CheckBox mCbImageOrigin;
    private TextView mTvImageSize;
    private TextView mTvSend;

    /**Intent传值使用，单张图片预览，如从相机拍照之后跳转过来*/
    public static final String FLAG_SINGLE_PREVIEW = "single_preview";

    private GalleryImageBean mImageBean;
    /**已选中项*/
    private List<SDFile> mSelectedList;
    /**正在显示的数据项*/
    private List<SDFile> mDataList = new ArrayList<SDFile>();

    /**是否单张图片预览，如从相机拍照之后跳转过来*/
    private boolean mSinglePreview;

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
        if (mSelectedList == null || mSelectedList.size() == 0) {
            finish();
            return;
        }

        mSinglePreview = getIntent().getBooleanExtra(FLAG_SINGLE_PREVIEW, false);

        mDataList.addAll(mSelectedList);

        List<String> urlList = new ArrayList<String>();
        for (SDFile file : mDataList) {
            if (file.getFileType() == ImagePickerActivity.FLAG_CAMERA) {
                continue;
            }
            urlList.add(file.getPath());
        }

        mImageBean = new GalleryImageBean();
        mImageBean.setUrlList(urlList);
        mImagePagerAdapter.setData(mImageBean);
        mImagePagerAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(0);

        updateSelectedInfo(mViewPager.getCurrentItem());
        if (mSinglePreview) {
            if (!TextUtils.isEmpty(mImagePickerOptions.getButtonText())) {
                mTvSend.setText(R.string.ok);
            } else {
                mTvSend.setText(R.string.send);
            }
            mTvSend.setTextColor(getResources().getColor(R.color.white));
            mTvSend.setBackgroundResource(R.drawable.im_btn_img_send_num);
        } else {
            onSelectedItemChanged();
        }
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
        SDFile sdFile = mDataList.get(position);
        mTvImageSize.setText(getString(R.string.origin_image_size, sdFile.getSize()));
        if (mPreviewActionProvider != null) {
            mPreviewActionProvider.setPreviewActionSelect(mSelectedList.contains(sdFile));
        }
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateSelectedInfo(mViewPager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mSinglePreview){
            return super.onCreateOptionsMenu(menu);
        }
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
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onPreviewActionSelected(boolean b) {
        SDFile sdFile = mDataList.get(mViewPager.getCurrentItem());
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
