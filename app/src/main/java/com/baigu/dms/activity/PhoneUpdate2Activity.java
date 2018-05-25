package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.PhoneUpdatePresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.PhoneUpdatePresenterImpl;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/11 22:23
 */
public class PhoneUpdate2Activity extends BaseActivity implements TextWatcher, SMSCodePresenter.SMSCodeView {
    private EditText mEtPhone;
    private Button mBtnNext;
    private SMSCodePresenter mSMSCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_update2);
        initToolBar();
        setTitle(R.string.update_phone);

        boolean validSource = getIntent().getBooleanExtra("checkOldPhone", false);
        if (!validSource) {
            finish();
            return;
        }

        mEtPhone = findView(R.id.et_tel);
        mEtPhone.addTextChangedListener(this);
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);

        mBtnNext = findView(R.id.btn_next);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEtPhone.getText().toString().trim();
                if (!StringUtils.isValidPhone(phone)) {
                    ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
                    return;
                }
                mSMSCodePresenter.sendSMSCode(User.SMSCodeType.NEW_PHONE, phone);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCodeButtonBg();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setCodeButtonBg();
    }

    private void setCodeButtonBg() {
        int alpha = mEtPhone.getText().length() > 0 ?  255 : Constants.BUTTON_UNABLE_ALPHA;
        mBtnNext.getBackground().mutate().setAlpha(alpha);
        mBtnNext.setClickable(alpha == 255);
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (result) {
            Intent intent = new Intent(this, PhoneUpdate3Activity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        }
    }
}
