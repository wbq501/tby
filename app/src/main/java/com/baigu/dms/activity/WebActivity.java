package com.baigu.dms.activity;

import android.annotation.TargetApi;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;

/**
 * @Description 网页
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initToolBar();
        String url = getIntent().getStringExtra("url");
        String content = getIntent().getStringExtra("content");
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        setTitle(title);
        initView();
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else {
            mWebView.loadData(StringUtils.decodeHtmlString(content), "text/html; charset=UTF-8", null);
        }
    }

    private void initView() {
        mProgressBar = findView(R.id.progress_bar);
        mWebView = findView(R.id.webView);
        ViewUtils.setupWebViewSettings(mWebView.getSettings());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e("error", "onReceivedHttpError");
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("error", "onReceivedError");
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
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
}
