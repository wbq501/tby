package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.URLFactory;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.presenter.UserPresenter;
import com.baigu.dms.presenter.impl.UserPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description 我的个人资料
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/30 12:33
 */
public class MyInfoActivity extends BaseActivity implements UserPresenter.UserView, View.OnClickListener {
    private ImageView mIvHead;
    private TextView mTvNickName;
    private TextView mTvRealName;
    private TextView mTvPhone;
    private TextView mTvEmail;
    private TextView mTvBankType;
    private TextView mTvBank;
    private TextView mTvAlipay;
    private TextView mTvWeixin;

    private TextView mTvLastLoginDate;

    private UserPresenter mUserPresenter;

    private boolean mBackToActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initToolBar();
        setTitle(R.string.my_info);

        mUserPresenter = new UserPresenterImpl(this, this);

        RxBus.getDefault().register(this);
        initView();
        initData();
        mUserPresenter.getMyInfo(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBackToActivity) {
            initData();
        }
        mBackToActivity = true;
    }

    private void initView() {
        mIvHead = findView(R.id.iv_head);
        mTvNickName = findView(R.id.tv_nickname);
        mTvRealName = findView(R.id.tv_realname);
        mTvPhone = findView(R.id.tv_phone);
        mTvEmail = findView(R.id.tv_email);
        mTvBankType = findView(R.id.tv_bank_type);
        mTvBank = findView(R.id.tv_bank);
        mTvAlipay = findView(R.id.tv_alipay);
        mTvWeixin = findView(R.id.tv_weixin);
        mTvLastLoginDate = findView(R.id.tv_lastlogindate);

        findViewById(R.id.ll_head).setOnClickListener(this);
        findViewById(R.id.ll_nick).setOnClickListener(this);
        findViewById(R.id.ll_phone).setOnClickListener(this);
        findViewById(R.id.ll_email).setOnClickListener(this);
        findViewById(R.id.ll_qrcode).setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
    }

    private void initData() {
        User user = UserCache.getInstance().getUser();
        mTvNickName.setText(user.getNick());
        mTvRealName.setText(user.getRealname());
        mTvPhone.setText(user.getCellphone());
        mTvEmail.setText(user.getEmail());
        mTvWeixin.setText(user.getWx_account() == null ? user.getWxaccount() : user.getWx_account());
        mTvAlipay.setText(user.getAlipayaccount());
        mTvBankType.setText(user.getBlankname());
        mTvBank.setText(user.getBlanknumber());
        mTvLastLoginDate.setText(user.getLastlogindate());
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.ll_head:
                startActivity(new Intent(this, MyHeadActivity.class));
                break;
            case R.id.ll_nick:
                startActivity(new Intent(this, MyNicknameActivity.class));
                break;
            case R.id.ll_phone:
                startActivity(new Intent(this, MyPhoneActivity.class));
                break;
            case R.id.ll_qrcode:
                startActivity(new Intent(this, MyQRCodeActivity.class));
                break;
            case R.id.ll_email:
                startActivity(new Intent(this, MyEmailActivity.class));
                break;
            case R.id.ll_weixin:
                startActivity(new Intent(this, MyWeixinActivity.class));
            default:
                break;
        }
    }

    @Override
    public void onGetMyInfo(User user) {
        if (user == null) {
            ViewUtils.showToastError(R.string.failed_load_user_info);
            finish();
            return;
        }
        Glide.with(this).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(mIvHead);
        mTvNickName.setText(user.getNick());
        mTvRealName.setText(user.getRealname());
        mTvPhone.setText(user.getCellphone());
        mTvEmail.setText(user.getEmail());
        mTvWeixin.setText(user.getWx_account() == null ? user.getWxaccount() : user.getWx_account());
        mTvLastLoginDate.setText(user.getLastlogindate());
        mTvBankType.setText(user.getBlankname());
        mTvBank.setText(user.getBlanknumber());
        mTvAlipay.setText(user.getAlipayaccount());
    }

    @Override
    public void onLoadToken(String token) {

    }

    @Override
    public void onSaveHead(boolean result) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        if (event.what == EventType.TYPE_UPDATE_HEAD_IMAGE) {
            User user = UserCache.getInstance().getUser();
            Glide.with(this).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(mIvHead);
        }
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
