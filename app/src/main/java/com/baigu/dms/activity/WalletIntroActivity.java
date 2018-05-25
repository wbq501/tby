package com.baigu.dms.activity;

import android.os.Bundle;

import com.baigu.dms.R;

/**
 * @Description 提现申请
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WalletIntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initToolBar();
        setTitle(R.string.wallet_intro);
        initView();
    }

    private void initView() {
    }


}
