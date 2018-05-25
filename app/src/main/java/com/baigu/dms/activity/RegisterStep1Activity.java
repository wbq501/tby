package com.baigu.dms.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.AllCapTransformationMethod;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.StepView;
import com.baigu.dms.common.view.textstyleplus.StyleBuilder;
import com.baigu.dms.common.view.textstyleplus.TextStyleItem;
import com.baigu.dms.domain.netservice.URLFactory;
import com.baigu.dms.presenter.RegisterPresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.RegisterPresenterImpl;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 12:05
 */
public class RegisterStep1Activity extends BaseActivity implements View.OnClickListener, RegisterPresenter.RegisterView, SMSCodePresenter.SMSCodeView {

    private EditText mEtInviteCode;
    private StepView mStepView;
    private Button mBtnInviteCode;
    private ConfirmDialog mConfirmDialog;

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step1);
        initToolBar();
        mToolbar.getBackground().mutate().setAlpha(255);
        setTitle(R.string.register);
        initView();
        mRegisterPresenter = new RegisterPresenterImpl(this, this);
    }

    private void initView() {
        mStepView = findView(R.id.stepView);
        mStepView.setImgRes(R.mipmap.step1, R.mipmap.step1_sel, R.mipmap.step2, R.mipmap.step2_sel, R.mipmap.step3, R.mipmap.step3_sel);
        mStepView.setText(getString(R.string.invite_code), getString(R.string.phone_num), getString(R.string.finish_register));
        mStepView.setCurStep(0);

        mBtnInviteCode = findView(R.id.btn_next);
        mBtnInviteCode.setOnClickListener(this);

        mEtInviteCode = findView(R.id.et_invite_code);
        mEtInviteCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                setCodeButtonBg();
            }
        });
        mEtInviteCode.setTransformationMethod(new AllCapTransformationMethod());
        TextView tvAgreement = findView(R.id.tv_agreement);

        new StyleBuilder()
                .addStyleItem(new TextStyleItem(getString(R.string.register_agreement1)))
                .addStyleItem(new TextStyleItem(getString(R.string.register_agreement2)).setClickListener(new TextStyleItem.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Intent intent = new Intent(RegisterStep1Activity.this, WebActivity.class);
                        intent.putExtra("title", getString(R.string.register_agreement_title));
                        intent.putExtra("url", URLFactory.getInstance().getRegisterAgreement());
                        startActivity(intent);
                    }
                }).setTextColor(getResources().getColor(R.color.colorPrimary)).setUnderLined(true))
                .addStyleItem(new TextStyleItem(getString(R.string.register_agreement3))).show(tvAgreement);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCodeButtonBg();
    }

    private void setCodeButtonBg() {
        int alpha = mEtInviteCode.getText().length() > 0 && mEtInviteCode.getText().length() > 0 ?  255 : Constants.BUTTON_UNABLE_ALPHA;
        mBtnInviteCode.getBackground().mutate().setAlpha(alpha);
        mBtnInviteCode.setClickable(alpha == 255);
        mToolbar.getBackground().mutate().setAlpha(255);
    }


    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.btn_next:
                String inviteCode = mEtInviteCode.getText().toString();
                if (TextUtils.isEmpty(inviteCode)) {
                    ViewUtils.showToastInfo(R.string.input_tip_invite_code);
                    return;
                }
                mRegisterPresenter.getUserByInviteCode(inviteCode);
            default:
                break;
        }
    }

    @Override
    public void onGetUserByInviteCode(final String result) {
        if (!TextUtils.isEmpty(result)) {
            if (mConfirmDialog == null) {
                mConfirmDialog = new ConfirmDialog(this, getString(R.string.invite_user_confirm, result));
                mConfirmDialog.setOKText(getString(R.string.yes));
                mConfirmDialog.setCancelText(getString(R.string.no));
            }
            mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                @Override
                public void onOKClick(View v) {
                    if (TextUtils.isEmpty(result.trim())) {
                        return;
                    }
                    Intent intent = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
                    intent.putExtra("inviteCode", mEtInviteCode.getText().toString().toUpperCase());
                    startActivity(intent);
                    finish();
                }
            });
            mConfirmDialog.show();
        } else {
            ViewUtils.showToastError(R.string.failed_get_invite_user);
        }
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {

    }

    @Override
    public void onRegister(boolean b) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRegisterPresenter != null) {
            mRegisterPresenter.onDestroy();
        }
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
    }
}
