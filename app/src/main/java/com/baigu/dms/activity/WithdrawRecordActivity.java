package com.baigu.dms.activity;

import android.os.Bundle;

import com.baigu.dms.R;

/**
 * @Description 提现记录
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WithdrawRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        initToolBar();
        setTitle(R.string.withdraw_record);
        initView();
    }

    private void initView() {
    }


}
