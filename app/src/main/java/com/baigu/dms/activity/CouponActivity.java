package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.baigu.dms.R;
import com.hyphenate.chat.ChatClient;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CouponActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developing);
        initToolBar();
        setTitle(R.string.coupon);
        initView();
    }

    private void initView() {
    }


}
