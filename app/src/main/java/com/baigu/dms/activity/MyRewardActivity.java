package com.baigu.dms.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.view.LoadingDialog;
import com.baigu.dms.common.view.X5WebView;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.LoginPresenter;
import com.baigu.dms.presenter.impl.LoginPresenterImpl;
import com.micky.logger.Logger;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MyRewardActivity extends BaseActivity implements LoginPresenter.LoginView {
    private X5WebView x5web;
    private LoadingDialog dialog;
    private String url;
    private SwipeRefreshLayout refresh;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreward_activity);
        initView();
    }

    private void initdata() {
        CookieSyncManager.createInstance(this);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        User user = UserCache.getInstance().getUser();
        url = BaseApplication.getContext().getString(R.string.my_reward_url);
        cm.setCookie(url,"phone="+user.getCellphone());
        cm.setCookie(url,"token="+ SPUtils.getObject("token",""));
        CookieSyncManager.getInstance().sync();
        x5web.loadUrl(url);
    }

    private void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        x5web = findView(R.id.x5web);
        refresh = findView(R.id.refresh);
        dialog = new LoadingDialog(MyRewardActivity.this,getString(R.string.loading));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                x5web.loadUrl(x5web.getUrl());
            }
        });

        x5web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (refresh.isRefreshing()){
                    refresh.setRefreshing(false);
                }
                dialog.dismiss();
            }
        });

        x5web.setOnScrollChangedCallback(new X5WebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t) {
                if (t == 0){
                    refresh.setEnabled(true);
                }else {
                    refresh.setEnabled(false);
                }
            }
        });
        mLoginPresenter = new LoginPresenterImpl(this, this);
        autoLogin();
    }

    private void autoLogin() {
        User user = UserCache.getInstance().getUser();
        if (user != null && !TextUtils.isEmpty(user.getLoginToken())) {
            try {
                dialog.show();
                mLoginPresenter.autoLogin();
            } catch (Exception e) {
                Logger.e(e, e.getMessage());
                gotoLogin();
            }
        } else {
            gotoLogin();
        }
    }

    private void gotoLogin() {
        startActivity(new Intent(MyRewardActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        if (x5web != null)
            x5web.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && x5web.canGoBack()){
            x5web.goBack();
            if(x5web.getUrl().equals(url)){
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLogin(boolean result) {
        if (result) {
            initdata();
        } else {
            gotoLogin();
        }
    }
}
