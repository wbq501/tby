package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.UpdateUserPresenter;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/11 22:23
 */
public class MyPhoneActivity extends BaseActivity implements SMSCodePresenter.SMSCodeView {
    private TextView mTvPhone;
    private SMSCodePresenter mSMSCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_phone);
        initToolBar();
        setTitle(R.string.my_phone);

        mTvPhone = findView(R.id.tv_phone);
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);

        User user = UserCache.getInstance().getUser();
        final String phone = user.getCellphone() == null ? "" : user.getCellphone();
        mTvPhone.setText(getString(R.string.binded_phone_tip, phone));

        findViewById(R.id.btn_update_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSMSCodePresenter.sendSMSCode(User.SMSCodeType.OLD_PHONE, phone);
            }
        });
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (result) {
            Intent intent = new Intent(MyPhoneActivity.this, PhoneUpdate1Activity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        }
    }
}
