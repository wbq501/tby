package com.baigu.dms.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.presenter.LoginPresenter;
import com.baigu.dms.presenter.impl.LoginPresenterImpl;

/**
 * @Description 登录
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/27 20:13
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.LoginView {

    public static final int REGIST_REQUEST_CODE = 1001;

    private LoginPresenter mLoginPresenterImpl;
    private EditText mEtAccount;
    private EditText mEtPasswd;
    private RelativeLayout mRlAccountDelete;
    private RelativeLayout mRlPasswdDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mLoginPresenterImpl = new LoginPresenterImpl(this, this);
        String lastLoginAccount = UserCache.getInstance().getAccount();
        mEtAccount.setText(lastLoginAccount);
    }

    private void initView() {
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_scan_register).setOnClickListener(this);
        findViewById(R.id.tv_find_passwd).setOnClickListener(this);
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPasswd = (EditText) findViewById(R.id.et_passwd);
        mRlAccountDelete = (RelativeLayout) findViewById(R.id.rl_account_delete);
        mRlPasswdDelete = (RelativeLayout) findViewById(R.id.rl_passwd_delete);
        mRlAccountDelete.setOnClickListener(this);
        mRlPasswdDelete.setOnClickListener(this);
        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mRlAccountDelete.setVisibility(editable.toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        mEtPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mRlPasswdDelete.setVisibility(editable.toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onLogin(boolean result) {
        if (result) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(mEtAccount.getText()) || TextUtils.isEmpty(mEtPasswd.getText())) {
                    ViewUtils.showToastError(R.string.account_or_pwd_empty);
                    return;
                }
                String appName = getString(R.string.app_name);
                String tip = getString(R.string.permission_sd_camera, appName, appName);
                PermissionRequest permissionRequest = new PermissionRequest(this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE);
                permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
                    @Override
                    public void onGrant() {
                        mLoginPresenterImpl.login(mEtAccount.getText().toString(), mEtPasswd.getText().toString());
                    }
                });
                permissionRequest.requestPermission(false);
                break;
            case R.id.tv_scan_register:
                startActivity(new Intent(LoginActivity.this, RegisterStep1Activity.class));
                break;
            case R.id.tv_find_passwd:
                startActivity(new Intent(LoginActivity.this, RefindPasswd1Activity.class));
                break;
            case R.id.rl_account_delete:
                mEtAccount.setText("");
                break;
            case R.id.rl_passwd_delete:
                mEtPasswd.setText("");
                break;
            default:
                break;
        }
    }

}
