package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * @Description 钱包安全
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WalletSecurityActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        initToolBar();
        setTitle(R.string.wallet_security);
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_pay_passwd).setOnClickListener(this);

        SwitchButton sbGesture = findView(R.id.sb_gesture);
        sbGesture.setChecked(!TextUtils.isEmpty(UserCache.getInstance().getGesture()));
        sbGesture.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_pay_passwd:
                startActivity(new Intent(this, UpdatePayPasswdActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sb_gesture:
                if (b) {
                    startActivity(new Intent(this, GestureEditActivity.class));
                } else {
                    UserCache.getInstance().setGesture("");
                }
                break;
            default:
                break;
        }
    }

}
