package com.baigu.dms.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.SwitchButton;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.presenter.UpdateUserPresenter;
import com.baigu.dms.presenter.impl.UpdateUserPresenterImpl;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class UpdatePasswdActivity extends BaseActivity implements UpdateUserPresenter.UpdateUserView {

    private EditText mEtOldPasswd;
    private EditText mEtNewPasswd;
    private EditText mEtNewPasswdAgain;
    private UpdateUserPresenter mUpdateUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_passwd);
        initToolBar();
        setTitle(R.string.update_login_pwd);
        initView();
        mUpdateUserPresenter = new UpdateUserPresenterImpl(this, this);
    }

    private void initView() {
        mEtOldPasswd = findView(R.id.et_old_passwd);
        mEtNewPasswd = findView(R.id.et_new_passwd);
        mEtNewPasswdAgain = findView(R.id.et_new_passwd_again);

        SwitchButton switchButton = findView(R.id.sb_pwd);
        switchButton.setOnSwitchStateListener(new SwitchButton.OnSwitchListener() {
            @Override
            public void onSwitched(boolean switched) {
                if (switched) {
                    mEtNewPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtNewPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mEtNewPasswd.setSelection(mEtNewPasswd.getText().length());
            }
        });
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

    @Override
    public void onUpdateUser(boolean result) {
        if (!result) {
            ViewUtils.showToastError(R.string.failed_update_passwd);
            return;
        }
        ViewUtils.showToastSuccess(R.string.success_refind_passwd);
        UserCache.getInstance().setUser(null);
        ViewUtils.logout(this);
    }

    private void save() {
        if (TextUtils.isEmpty(mEtOldPasswd.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_old_passwd);
            return;
        }
        if (TextUtils.isEmpty(mEtNewPasswd.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_new_passwd);
            return;
        }
        if (mEtNewPasswd.getText().toString().trim().length() < 6 || mEtNewPasswd.getText().toString().trim().length() > 20) {
            ViewUtils.showToastError(R.string.pls_input_passwd_register);
            return;
        }
        if (!mEtNewPasswdAgain.getText().toString().trim().equals(mEtNewPasswd.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_new_old_passwd_not_same);
            return;
        }
        mUpdateUserPresenter.updatePasswd(mEtOldPasswd.getText().toString().trim(), mEtNewPasswd.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateUserPresenter != null) {
            mUpdateUserPresenter.onDestroy();
        }
    }
}
