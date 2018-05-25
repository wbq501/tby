package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.baigu.dms.R;
import com.baigu.dms.adapter.ImageDeletePagerAdapter;
import com.baigu.dms.common.utils.ViewUtils;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImageDeletePreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private List<String> mUrlList;

    private ImageDeletePagerAdapter mImagePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setBackgroundDrawableResource(R.color.black);
        }
        setContentView(R.layout.activity_image_delete_preview);
        initToolBar();
        setTitle(R.string.preview);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.black_a));

        mUrlList = getIntent().getStringArrayListExtra("urlList");
        if (mUrlList == null || mUrlList.size() <= 0) {
            finish();
            return;
        }
        initView();
        mImagePagerAdapter.setData(mUrlList);
        mImagePagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mViewPager = findView(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mImagePagerAdapter = new ImageDeletePagerAdapter(this);
        mViewPager.setAdapter(mImagePagerAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            Intent intent = getIntent();
            intent.putExtra("deleteUrl", mUrlList.get(mViewPager.getCurrentItem()));
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
