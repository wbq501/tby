package com.baigu.dms.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.baigu.dms.R;
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
public class MyWeixinActivity extends BaseActivity implements UpdateUserPresenter.UpdateUserView {
    private EditText mEtWeixin;
    private UpdateUserPresenter mUserUpdatePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_weixin);
        initToolBar();
        setTitle(R.string.weixin);

        mEtWeixin = findView(R.id.et_weixin);

        User user = UserCache.getInstance().getUser();
        mEtWeixin.setText(user.getWx_account() == null ? "" : user.getWx_account());
        mEtWeixin.setSelection(mEtWeixin.getText().toString().length());

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
                String wexin = mEtWeixin.getText().toString();
                if (TextUtils.isEmpty(wexin)) {
                    ViewUtils.showToastError(R.string.input_tip_weixin);
                    return super.onOptionsItemSelected(item);
                }
                mUserUpdatePresenter.updateWeixin(wexin);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateUser(boolean result) {
        if (result) {
            ViewUtils.showToastInfo(R.string.success_user_update);
            finish();
        } else {
            ViewUtils.showToastInfo(R.string.failed_user_update);
        }
    }
}
