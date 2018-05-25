package com.baigu.dms.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.APKUtils;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.presenter.AboutUsPresenter;
import com.baigu.dms.presenter.impl.AboutUsPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Description 关于我们
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AppUseAgreementActivity extends BaseActivity implements AboutUsPresenter.AboutUsView {

    private WebView mWebView;
    private AboutUsPresenter mAboutUsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_use_agreement);
        initToolBar();
        setTitle(R.string.app_use_agreement);
        initView();
        mAboutUsPresenter = new AboutUsPresenterImpl(this, this);

        Flowable.timer(100L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mAboutUsPresenter.getAboutUs();
                    }
                });
    }

    private void initView() {
        mWebView = findView(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setDomStorageEnabled(false);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        if (Constants.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView();
        }
    }

    @TargetApi(11)
    private void fixWebView() {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
    }

    @Override
    public void onGetAboutUs(String result) {
        if (result != null) {
            mWebView.loadData(StringUtils.decodeHtmlString(result), "text/html; charset=UTF-8", null);
        }
    }
}
