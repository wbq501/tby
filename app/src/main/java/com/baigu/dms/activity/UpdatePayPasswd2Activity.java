package com.baigu.dms.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.SwitchButton;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.ChangePayPresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.impl.ChangePayPresenterImpl;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;

import java.util.Timer;
import java.util.TimerTask;

public class UpdatePayPasswd2Activity extends BaseActivity implements View.OnClickListener,SMSCodePresenter.SMSCodeView,ChangePayPresenter.ChangePayView{
    public static final int FLAG_COUNTING = 1001;
    public static final int FLAG_COUNT_FINISH = 1002;

    private EditText et_idcard_num,et_phone_num,et_passwd,et_passwd_again,et_msg_code;
    private SwitchButton sb_pwd;
    private Button mBtnCode;

    private boolean mIsSendingCode = false;
    private boolean mIsPause;
    private Timer mTimer;
    private int mTimeCount = Constants.AUTH_CODE_TIME;
    private SMSCodePresenter mSMSCodePresenter;
    private ChangePayPresenter changePayPresenter;

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
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pay_passwd2);
        initToolBar();
        setTitle(R.string.forget_pay_passwd);
        initView();
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);
        changePayPresenter = new ChangePayPresenterImpl(this,this);
    }

    private void initView() {
        et_idcard_num = findView(R.id.et_idcard_num);
        et_phone_num = findView(R.id.et_phone_num);
        et_passwd = findView(R.id.et_passwd);
        et_passwd_again = findView(R.id.et_passwd_again);
        et_msg_code = findView(R.id.et_msg_code);

        sb_pwd = findView(R.id.sb_pwd);
        sb_pwd.setOnSwitchStateListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitched(boolean switched) {
                if (switched) {
                    et_passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                et_passwd.setSelection(et_passwd.getText().length());
            }
        });
        mBtnCode = findView(R.id.btn_send_code);
        mBtnCode.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_ok:
                changepsd();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changepsd() {
        String idcard = et_idcard_num.getText().toString().trim();
        String phonenum = et_phone_num.getText().toString().trim();
        String passd = et_passwd.getText().toString().trim();
        String pasd_again = et_passwd_again.getText().toString().trim();
        String code = et_msg_code.getText().toString().trim();

        User user = UserCache.getInstance().getUser();

        if (TextUtils.isEmpty(idcard)){
            ViewUtils.showToastError(R.string.input_tip_idcard);
            return;
        }
        if (!idcard.equals(user.getIdcard())){
            ViewUtils.showToastError(R.string.is_idcard);
            return;
        }
        if (phonenum.length() != 11){
            ViewUtils.showToastError(R.string.input_tip_take_phone);
            return;
        }
        if (!phonenum.equals(user.getCellphone())){
            ViewUtils.showToastError(R.string.is_phone);
            return;
        }
        if (passd.length() != 6){
            ViewUtils.showToastError(R.string.pay_password);
            return;
        }
        if (!passd.equals(pasd_again)){
            ViewUtils.showToastError(R.string.is_paypsd);
            return;
        }
        if (TextUtils.isEmpty(code)){
            ViewUtils.showToastError(R.string.tip_msg_code);
            return;
        }
        changePayPresenter.resetpay(phonenum,code,passd,idcard);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_code:
                ViewUtils.hideInputMethod(this);
                if (mIsSendingCode) {
                    return;
                }
                mIsSendingCode = true;
                mBtnCode.setText(getString(R.string.code_sent, String.valueOf(mTimeCount)));
                mBtnCode.setTextColor(getResources().getColor(R.color.code_unable_get));
                startCount();
                String phone = UserCache.getInstance().getUser().getCellphone();
                mSMSCodePresenter.sendSMSCode(User.SMSCodeType.UPD_PAY_PWD, phone);
                break;
        }
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
    }

    @Override
    public void onDestroy() {
        clearCounter();
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

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (!result) {
            clearCounter();
        }
    }

    @Override
    public void changePayState(String state) {
        if (TextUtils.isEmpty(state)){
            ViewUtils.showToastSuccess("修改失败");
            return;
        }
        ViewUtils.showToastSuccess(state);
        String change_success = getString(R.string.change_success);
        if (state.equals(change_success)){
            finish();
        }
    }
}
