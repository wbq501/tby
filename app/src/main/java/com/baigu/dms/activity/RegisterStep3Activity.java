package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.StepView;
import com.baigu.dms.common.view.SwitchButton;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.RegisterPresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.RegisterPresenterImpl;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 12:05
 */
public class RegisterStep3Activity extends BaseActivity implements View.OnClickListener, TextWatcher, RegisterPresenter.RegisterView, SMSCodePresenter.SMSCodeView {
    public static final int FLAG_COUNTING = 1001;
    public static final int FLAG_COUNT_FINISH = 1002;
    public static final int REQUEST_CODE_BANK_TYPE_SELECT = 30001;

    private StepView mStepView;
    private TextView mTvTel;
    private EditText mEtPasswd;
    private EditText mEtMsgCode;
    private EditText mEtRealname;
//    private EditText mEtBankType;
//    private EditText mEtBank;
//    private EditText mEtAlipay;
    private EditText mEtWeixin;
    private TextView mTvInviteCode;
    private Button mBtnCode;
    private Button mBtnRegister;
    private boolean mRegistinng;
    private BankType mBankType;

    private Timer mTimer;
    private boolean mIsSendingCode = false;
    private int mTimeCount = Constants.AUTH_CODE_TIME;
    private boolean mIsPause;

    private RegisterPresenter mRegisterPresenter;
    private SMSCodePresenter mSMSCodePresenter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case FLAG_COUNTING:
                    if (mIsPause) {
                        return;
                    }
                    mIsSendingCode = true;
                    mBtnCode.setText(getString(R.string.code_sent, String.valueOf(mTimeCount)));
                    mBtnCode.setTextColor(getResources().getColor(R.color.code_unable_get));
                    break;
                case FLAG_COUNT_FINISH:
                    mIsSendingCode = false;
                    if (mIsPause) return;
                    mBtnCode.setText(R.string.get_msg_code);
                    mBtnCode.setTextColor(getResources().getColor(R.color.black));
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);
        initToolBar();
        setTitle(R.string.register);
        String phone = getIntent().getStringExtra("phone");
        String inviteCode = getIntent().getStringExtra("inviteCode");
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(inviteCode)) {
            finish();
            return;
        }
        initView();
        mRegisterPresenter = new RegisterPresenterImpl(this, this);
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);

        mTvTel.setText(phone);
        mTvInviteCode.setText(inviteCode);

        mIsSendingCode = true;
        mBtnCode.setText(getString(R.string.code_sent, String.valueOf(mTimeCount)));
        mBtnCode.setTextColor(getResources().getColor(R.color.code_unable_get));
        startCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPause = false;
        if (mIsSendingCode) {
            mBtnCode.setText(getString(R.string.code_sent, String.valueOf(mTimeCount)));
            mBtnCode.setTextColor(getResources().getColor(R.color.code_unable_get));
        } else {
            mBtnCode.setText(R.string.get_msg_code);
            mBtnCode.setTextColor(getResources().getColor(R.color.article_content));
        }
        setCodeButtonBg();
    }

    private void initView() {
        mStepView = findView(R.id.stepView);
        mStepView.setImgRes(R.mipmap.step1, R.mipmap.step1_sel, R.mipmap.step2, R.mipmap.step2_sel, R.mipmap.step3, R.mipmap.step3_sel);
        mStepView.setText(getString(R.string.invite_code), getString(R.string.phone_num), getString(R.string.finish_register));
        mStepView.setCurStep(2);

        mTvTel = findView(R.id.tv_tel);
        mEtPasswd = findView(R.id.et_passswd);
        mEtPasswd.addTextChangedListener(this);
        mTvInviteCode = findView(R.id.tv_invite_code);
        mEtMsgCode = findView(R.id.et_msg_code);
        mEtMsgCode.addTextChangedListener(this);
        mEtRealname = findView(R.id.et_realname);
        mEtRealname.addTextChangedListener(this);
//        mEtBankType = findView(R.id.et_bank_type);
//        mEtBankType.setOnClickListener(this);
//        mEtBank = findView(R.id.et_bank);
//        mEtAlipay = findView(R.id.et_alipay);
        mEtWeixin = findView(R.id.et_weixin);
        mEtWeixin.addTextChangedListener(this);

        mBtnCode = findView(R.id.btn_msg_code);
        mBtnCode.setOnClickListener(this);

        mBtnRegister = findView(R.id.btn_register);
        mBtnRegister.setOnClickListener(this);

        SwitchButton switchButton = findView(R.id.sb_pwd);
        switchButton.setOnSwitchStateListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitched(boolean switched) {
                if (switched) {
                    mEtPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mEtPasswd.setSelection(mEtPasswd.getText().length());
            }
        });
    }

    @Override
    public void onDestroy() {
        clearCounter();
        if (mRegisterPresenter != null) {
            mRegisterPresenter.onDestroy();
        }
        if (mSMSCodePresenter != null) {
            mSMSCodePresenter.onDestroy();
        }
        super.onDestroy();
    }

    private void startCount() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimeCount = Constants.AUTH_CODE_TIME;
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mTimeCount--;
                if (mTimeCount <= 0) {
                    mHandler.sendEmptyMessage(FLAG_COUNT_FINISH);
                    cancel();
                    mTimer = null;
                } else {
                    mHandler.sendEmptyMessage(FLAG_COUNTING);
                }
            }
        }, 0, 1000);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setCodeButtonBg();
    }

    private void setCodeButtonBg() {
        int alpha = mTvTel.getText().length() > 0
                && mEtMsgCode.getText().length() > 0
                && mEtRealname.getText().length() > 0
                && mTvInviteCode.getText().length() > 0
//                && mEtBankType.getText().length() > 0
//                && mEtBank.getText().length() > 0
//                && mEtAlipay.getText().length() > 0
                && mEtWeixin.getText().length() > 0
                && mEtPasswd.getText().length()> 0 ?  255 : Constants.BUTTON_UNABLE_ALPHA;
        mBtnRegister.getBackground().mutate().setAlpha(alpha);
        mBtnRegister.setClickable(alpha == 255);
    }

    private void clearCounter() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        mIsSendingCode = false;
        mTimeCount = Constants.AUTH_CODE_TIME;
        if (!mIsPause && mBtnCode != null) {
            mBtnCode.setText(R.string.get_msg_code);
            mBtnCode.setTextColor(getResources().getColor(R.color.article_content));
        }
    }

    private void register() {
        String phone = mTvTel.getText().toString().trim();
        String realname = mEtRealname.getText().toString().trim();
        String password = mEtPasswd.getText().toString().trim();
        String code = mEtMsgCode.getText().toString().trim();
        String inviteCode = mTvInviteCode.getText().toString().trim();
//        String bankTypeName = mEtBankType.getText().toString().trim();
//        String bank = mEtBank.getText().toString().trim();
//        String alipay = mEtAlipay.getText().toString().trim();
        String weixin = mEtWeixin.getText().toString().trim();

        if (mRegistinng) {
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            ViewUtils.showToastInfo(R.string.input_tip_phone);
            return;
        }
        if (TextUtils.isEmpty(realname)) {
            ViewUtils.showToastInfo(R.string.input_tip_realname);
            return;
        }

        if (!StringUtils.isValidPhone(phone)) {
            ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
            return;
        }

//        if (TextUtils.isEmpty(bankTypeName)) {
//            ViewUtils.showToastInfo(R.string.input_tip_bank_type);
//            return;
//        }
//        if (TextUtils.isEmpty(bank)) {
//            ViewUtils.showToastInfo(R.string.input_tip_bank);
//            return;
//        }
//        if (TextUtils.isEmpty(alipay)) {
//            ViewUtils.showToastInfo(R.string.input_tip_alipay);
//            return;
//        }
        if (TextUtils.isEmpty(weixin)) {
            ViewUtils.showToastInfo(R.string.input_tip_weixin);
            return;
        }
        if (TextUtils.isEmpty(inviteCode)) {
            ViewUtils.showToastInfo(R.string.invite_code_error);
            return;
        }
        if (password.length() < 6 || password.length() > 30) {
            ViewUtils.showToastInfo(R.string.input_tip_tel_pwd_length_error);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ViewUtils.showToastInfo(R.string.input_tip_msg_code);
            return;
        }

        mRegistinng = true;

        mRegisterPresenter.register(phone, realname, password, code, inviteCode,
//                mBankType.getIds(),
//                bank,
//                alipay,
                weixin );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BANK_TYPE_SELECT) {
            mBankType = data.getParcelableExtra("bankType");
            if (mBankType != null) {
//                mEtBankType.setText(mBankType.getNames());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.et_bank_type:
                startActivityForResult(new Intent(this, BankTypeSelectorActivity.class), REQUEST_CODE_BANK_TYPE_SELECT);
                break;
            case R.id.btn_msg_code:
                ViewUtils.hideInputMethod(this);
                if (mIsSendingCode) {
                    return;
                }

                String phone = mTvTel.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ViewUtils.showToastInfo(R.string.input_tip_phone);
                    return;
                }

                if (!StringUtils.isValidPhone(phone)) {
                    ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
                    return;
                }

                mIsSendingCode = true;
                mBtnCode.setText(getString(R.string.code_sent, String.valueOf(mTimeCount)));
                mBtnCode.setTextColor(getResources().getColor(R.color.code_unable_get));
                startCount();
                mSMSCodePresenter.sendSMSCode(User.SMSCodeType.REGISTER, phone);
                break;
            case R.id.btn_register:
                register();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetUserByInviteCode(String result) {

    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (!result) {
            clearCounter();
        }
    }

    @Override
    public void onRegister(boolean b) {
        mRegistinng = false;
        if (b) {
            Intent intent = new Intent(RegisterStep3Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
