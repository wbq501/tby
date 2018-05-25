package com.baigu.dms.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.UpdateUserPresenter;
import com.baigu.dms.presenter.impl.UpdateUserPresenterImpl;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/11 22:23
 */
public class MyEmailActivity extends BaseActivity implements UpdateUserPresenter.UpdateUserView {
    private EditText mEtEmail;
    private UpdateUserPresenter mUserUpdatePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_email);
        initToolBar();
        setTitle(R.string.email);

        mEtEmail = findView(R.id.et_email);

        User user = UserCache.getInstance().getUser();
        mEtEmail.setText(user.getEmail() == null ? "" : user.getEmail());
        mEtEmail.setSelection(mEtEmail.getText().toString().length());

        mUserUpdatePresenter = new UpdateUserPresenterImpl(this, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_ok:
                String email = mEtEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    ViewUtils.showToastError(R.string.input_tip_email);
                    return super.onOptionsItemSelected(item);
                }
                if (!StringUtils.isEmail(email)) {
                    ViewUtils.showToastError(R.string.input_tip_invalid_email);
                    return super.onOptionsItemSelected(item);
                }
                mUserUpdatePresenter.updateEmail(email);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateUser(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_user_update);
            finish();
        } else {
            ViewUtils.showToastInfo(R.string.failed_user_update);
        }
    }
}
