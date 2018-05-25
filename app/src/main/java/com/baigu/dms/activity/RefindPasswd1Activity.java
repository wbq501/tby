package com.baigu.dms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 12:05
 */
public class RefindPasswd1Activity extends BaseActivity implements View.OnClickListener, SMSCodePresenter.SMSCodeView {

    private EditText mEtTel;
    private Button mBtnSendCode;

    private SMSCodePresenter mSMSCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refind_passwd1);
        initToolBar();
        mToolbar.getBackground().mutate().setAlpha(255);
        setTitle(R.string.refind_passwd);
        initView();
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCodeButtonBg();
    }

    private void initView() {

        mEtTel = findView(R.id.et_tel);
        mEtTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                setCodeButtonBg();
            }
        });

        mBtnSendCode = findView(R.id.btn_send_code);
        mBtnSendCode.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REGIST_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK, getIntent());
            finish();
        }
    }

    private void setCodeButtonBg() {
        int alpha = mEtTel.getText().length() > 0 && mEtTel.getText().length() > 0 ?  255 : Constants.BUTTON_UNABLE_ALPHA;
        mBtnSendCode.getBackground().mutate().setAlpha(alpha);
        mBtnSendCode.setClickable(alpha == 255);
        mToolbar.getBackground().mutate().setAlpha(255);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.btn_send_code:
                String phone = mEtTel.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ViewUtils.showToastInfo(R.string.input_tip_phone);
                    return;
                }

                if (!StringUtils.isValidPhone(phone)) {
                    ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
                    return;
                }
                mSMSCodePresenter.sendSMSCode(User.SMSCodeType.UPD_LOGIN_PWD,  phone);
            default:
                break;
        }
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (result) {
            Intent intent = new Intent(this, RefindPasswd2Activity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        }
    }

}
