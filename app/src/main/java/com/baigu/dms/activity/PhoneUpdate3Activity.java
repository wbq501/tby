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
public class PhoneUpdate3Activity extends BaseActivity implements View.OnClickListener, TextWatcher,  SMSCodePresenter.SMSCodeView, PhoneUpdatePresenter.PhoneUpdateView {
    public static final int FLAG_COUNTING = 1001;
    public static final int FLAG_COUNT_FINISH = 1002;

    private TextView mTvTel;
    private EditText mEtMsgCode;
    private Button mBtnCode;
    private Button mBtnNext;
    private boolean mSubmiting;

    private Timer mTimer;
    private boolean mIsSendingCode = false;
    private int mTimeCount = Constants.AUTH_CODE_TIME;
    private boolean mIsPause;

    private PhoneUpdatePresenter mPhoneUpdatePresenter;
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
        setContentView(R.layout.activity_phone_update3);
        initToolBar();
        setTitle(R.string.update_phone);
        String phone = getIntent().getStringExtra("phone");
        if (TextUtils.isEmpty(phone)) {
            finish();
            return;
        }
        initView();
        mPhoneUpdatePresenter = new PhoneUpdatePresenterImpl(this, this);
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
        mEtMsgCode = findView(R.id.et_msg_code);
        mEtMsgCode.addTextChangedListener(this);

        mBtnCode = findView(R.id.btn_msg_code);
        mBtnCode.setOnClickListener(this);

        mBtnNext = findView(R.id.btn_next);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        clearCounter();
        if (mPhoneUpdatePresenter != null) {
            mPhoneUpdatePresenter.onDestroy();
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
                && mEtMsgCode.getText().length() > 0 ?  255 : Constants.BUTTON_UNABLE_ALPHA;
        mBtnNext.getBackground().mutate().setAlpha(alpha);
        mBtnNext.setClickable(alpha == 255);
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

    private void checkOldPhone() {
        String phone = mTvTel.getText().toString();
        String code = mEtMsgCode.getText().toString();

        if (mSubmiting) {
            return;
        }

        if (!StringUtils.isValidPhone(phone)) {
            ViewUtils.showToastInfo(R.string.input_tip_tel_length_error);
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ViewUtils.showToastInfo(R.string.input_tip_msg_code);
            return;
        }

        mSubmiting = true;
        mPhoneUpdatePresenter.updatePhone(phone, code);

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

                String phone = mTvTel.getText().toString().trim();
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
            case R.id.btn_next:
                checkOldPhone();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckPhone(boolean b) {

    }

    @Override
    public void onUpdatePhone(boolean b) {
        mSubmiting = false;
        if (b) {
            ViewUtils.logout(this);
        }
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (!result) {
            clearCounter();
        }
    }

    private void sendMsgCode(String phone) {
        mSMSCodePresenter.sendSMSCode(User.SMSCodeType.NEW_PHONE, phone);
    }
}
