package com.baigu.dms.activity;

import android.os.Bundle;

import com.baigu.dms.R;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class ScoreShopActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developing);
        initToolBar();
        setTitle(R.string.score_shop);
        initView();
    }

    private void initView() {
    }


}
