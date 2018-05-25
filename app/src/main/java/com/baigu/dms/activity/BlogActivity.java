package com.baigu.dms.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.view.LoadingDialog;
import com.baigu.dms.common.view.X5WebView;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class BlogActivity extends BaseActivity{

    private X5WebView x5web;
    private LoadingDialog dialog;
    private String url;
    private SwipeRefreshLayout refresh;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_activity);
        initToolBar();
        title = getIntent().getStringExtra("title");
        setTitle(title);
        initView();
        initdata();
    }

    private void initdata() {
        CookieSyncManager.createInstance(this);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        User user = UserCache.getInstance().getUser();
        url = BaseApplication.getContext().getString(R.string.base_url);
        cm.setCookie(url,"tale_remember_author="+user.getRealName());
        CookieSyncManager.getInstance().sync();
        x5web.loadUrl(url);
        dialog.show();
    }

    private void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        x5web = findView(R.id.x5web);
        refresh = findView(R.id.refresh);
        dialog = new LoadingDialog(BlogActivity.this,getString(R.string.loading));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                x5web.loadUrl(x5web.getUrl());
            }
        });
        ImageButton ib_back = findView(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
