package com.baigu.dms.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.SharePopView;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class MyInviteCodeActivity extends BaseActivity {
    private TextView mTvInviteCode;
    private SharePopView mSharePopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invite_code);
        initToolBar();
        setTitle(R.string.my_invite_code);

        initView();
        User user = UserCache.getInstance().getUser();

        mTvInviteCode.setText(user.getInvitecode());
    }

    private void initView() {
        mTvInviteCode = findView(R.id.tv_invite_code);
        final TextView tvCopy = findView(R.id.tv_copy);
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.copy(MyInviteCodeActivity.this, mTvInviteCode.getText().toString().trim());
                ViewUtils.showToastSuccess(R.string.success_copy);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_share) {
            share();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSharePopView != null) {
            mSharePopView.dismiss();
        }
    }

    private void share() {
        if (mSharePopView == null) {
            mSharePopView = new SharePopView(this);
        }
        mSharePopView.showAtLocation(this.findViewById(R.id.ll_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
