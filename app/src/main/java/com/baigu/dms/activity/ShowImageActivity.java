package com.baigu.dms.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;


import com.baigu.dms.R;
import com.baigu.dms.adapter.ShowImageAdapter;

import java.util.ArrayList;


public class ShowImageActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ShowImageAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        initView();
    }

    private void initView() {
        ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        int position = getIntent().getIntExtra("position", 0);
        mViewPager = findViewById(R.id.vp_shop);
        adapter = new ShowImageAdapter(this);
        adapter.setData(data);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.show_img_close);
    }
}
