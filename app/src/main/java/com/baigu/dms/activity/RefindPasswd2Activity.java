package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
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
import com.baigu.dms.common.view.SwitchButton;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.RefindPasswdPresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.RefindPasswdPresenterImpl;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 12:05
 */
public class RefindPasswd2Activity extends BaseActivity implements View.OnClickListener, TextWatcher, SMSCodePresenter.SMSCodeView, RefindPasswdPresenter.RefindPasswdView {
    public static final int FLAG_COUNTING = 1001;
    public static final int FLAG_COUNT_FINISH = 1002;

    private TextView mTvTel;
    private EditText mEtPasswd;
    private EditText mEtMsgCode;
    private Button mBtnCode;
    private Button mBtnRegister;
    private boolean mRefinding;

    private Timer mTimer;
    private boolean mIsSendingCode = false;
    private int mTimeCount = Constants.AUTH_CODE_TIME;
    private boolean mIsPause;

    private RefindPasswdPresenter mRefindPasswdPresenter;
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
        setContentView(R.layout.activity_refind_passwd2);
        initToolBar();
        setTitle(R.string.refind_passwd);
        String phone = getIntent().getStringExtra("phone");

        initView();
        mRefindPasswdPresenter = new RefindPasswdPresenterImpl(this, this);
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);
        mTvTel.setText(phone);

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

        mTvTel = findView(R.id.tv_tel);
        mEtPasswd = findView(R.id.et_passswd);
        mEtPasswd.addTextChangedListener(this);
        mEtMsgCode = findView(R.id.et_msg_code);
        mEtMsgCode.addTextChangedListener(this);

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
        setCodeButtonBg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearCounter();
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

    private void refindPasswd() {
        String phone = mTvTel.getText().toString();
        String password = mEtPasswd.getText().toString();
        String code = mEtMsgCode.getText().toString();

        if (mRefinding) {
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            ViewUtils.showToastInfo(R.string.input_tip_phone);
            return;
        }

        if (!StringUtils.isValidPhone(phone)) {
            ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ViewUtils.showToastInfo(R.string.tip_passwd);
            return;
        }

        if (password.length() < 6 || password.length() > 20) {
            ViewUtils.showToastInfo(R.string.input_tip_tel_pwd_length_error);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ViewUtils.showToastInfo(R.string.input_tip_msg_code);
            return;
        }

        mRefinding = true;
        mRefindPasswdPresenter.refindPasswd(phone, password, code);

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
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
                sendMsgCode(phone);
                break;
            case R.id.btn_register:
                refindPasswd();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (!result) {
            clearCounter();
        }
    }

    @Override
    public void onRefindPasswd(boolean b) {
        mRefinding = false;
        if (b) {
            Intent intent = new Intent(RefindPasswd2Activity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void sendMsgCode(String phone) {
        mSMSCodePresenter.sendSMSCode(User.SMSCodeType.UPD_LOGIN_PWD, phone);
    }
}
