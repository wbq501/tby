package com.baigu.dms.activity;

import android.content.Intent;
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
import android.widget.LinearLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.SwitchButton;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.CertificationPresenter;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.baigu.dms.presenter.UpdateUserPresenter;
import com.baigu.dms.presenter.impl.CertificationPresenterImpl;
import com.baigu.dms.presenter.impl.SMSCodePresenterImpl;
import com.baigu.dms.presenter.impl.UpdateUserPresenterImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class UpdatePayPasswdActivity extends BaseActivity implements UpdateUserPresenter.UpdateUserView, SMSCodePresenter.SMSCodeView, View.OnClickListener, CertificationPresenter.CertificationView {
    public static final int FLAG_COUNTING = 1001;
    public static final int FLAG_COUNT_FINISH = 1002;
    public static final int TYPE_INITIALIZE = 1;

    private EditText mEtPasswd;
    private EditText mEtPasswdAgain;
    private EditText mEtMsgCode;
    private Button mBtnCode;
    private LinearLayout mLayoutCode;
    private View mViewLine;

    private Timer mTimer;
    private boolean mIsSendingCode = false;
    private int mTimeCount = Constants.AUTH_CODE_TIME;
    private boolean mIsPause;
    private int type;
    private String mIDCardFrontPath;
    private String mIDCardBackPath;
    private String idCard;

    private LinearLayout ll_oldpaypsd;//是否显示老支付密码 2018.5.10
    private View line_oldpay;
    private EditText et_old_passwd;
    private Button btn_forgetpsd;

    private UpdateUserPresenter mUpdateUserPresenter;
    private SMSCodePresenter mSMSCodePresenter;
    private CertificationPresenter mCertificationPresenter;

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
        setContentView(R.layout.activity_update_pay_passwd);
        initToolBar();
        setTitle(R.string.pay_passwd);
        initView();
        mUpdateUserPresenter = new UpdateUserPresenterImpl(this, this);
        mSMSCodePresenter = new SMSCodePresenterImpl(this, this);
        mCertificationPresenter = new CertificationPresenterImpl(this, this);
    }

    private void initView() {
        mEtPasswd = findView(R.id.et_passwd);
        mEtPasswdAgain = findView(R.id.et_passwd_again);
        mEtMsgCode = findView(R.id.et_msg_code);
        mLayoutCode = findViewById(R.id.ll_pay_passwd_code);
        mViewLine = findViewById(R.id.view_line);
        ll_oldpaypsd = findView(R.id.ll_oldpaypsd);
        line_oldpay = findView(R.id.line_oldpay);
        et_old_passwd = findView(R.id.et_old_passwd);
        btn_forgetpsd = findView(R.id.btn_forgetpsd);
        btn_forgetpsd.setOnClickListener(this);

        mBtnCode = findView(R.id.btn_send_code);
        mBtnCode.setOnClickListener(this);

        type = getIntent().getIntExtra("type", 0);
        mIDCardFrontPath = getIntent().getStringExtra("frontPath");
        mIDCardBackPath = getIntent().getStringExtra("backPath");
        idCard = getIntent().getStringExtra("idCard");

        if (type != 0){
            ll_oldpaypsd.setVisibility(View.GONE);
            line_oldpay.setVisibility(View.GONE);
        }

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

        if (type == TYPE_INITIALIZE) {
            mLayoutCode.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        } else {
            mLayoutCode.setVisibility(View.VISIBLE);
            mViewLine.setVisibility(View.VISIBLE);
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
        if (mUpdateUserPresenter != null) {
            mUpdateUserPresenter.onDestroy();
        }
        super.onDestroy();
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
                save();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onUpdateUser(boolean result) {
        if (result) {
            finish();
        }
    }

    @Override
    public void onSendSMSCode(boolean result, String phone) {
        if (!result) {
            clearCounter();
        }
    }

    private void save() {
        if (TextUtils.isEmpty(mEtPasswd.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_passwd);
            return;
        }
        if (mEtPasswdAgain.getText().toString().trim().length() < 6 || mEtPasswdAgain.getText().toString().trim().length() > 20) {
            ViewUtils.showToastError(R.string.pay_password);
            return;
        }
        if (!mEtPasswd.getText().toString().trim().equals(mEtPasswdAgain.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_new_old_passwd_not_same);
            return;
        }
        if (type == TYPE_INITIALIZE) {
            mCertificationPresenter.saveIDCard(idCard, mIDCardFrontPath, mIDCardBackPath,mEtPasswd.getText().toString().trim());
        } else {
            if (TextUtils.isEmpty(mEtMsgCode.getText().toString().trim())) {
                ViewUtils.showToastError(R.string.input_tip_msg_code);
                return;
            }
            mUpdateUserPresenter.updatePayPasswd(UserCache.getInstance().getUser().getCellphone(), mEtPasswd.getText().toString().trim(), mEtMsgCode.getText().toString().trim());
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.btn_forgetpsd:
                startActivity(new Intent(UpdatePayPasswdActivity.this,UpdatePayPasswd2Activity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveIDCard(boolean b) {

        if (!b) {
            ViewUtils.showToastError(getString(R.string.init_pwd_failed));
            return;
        }
        UserCache.getInstance().getUser().setIdcardstatus(User.IDCardStatus.VERIFY_DOING);
        Intent intent = new Intent(this, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onLoadImage(String imgURL) {

    }

    @Override
    public void onLoadImageBack(String imgURL) {

    }
}
