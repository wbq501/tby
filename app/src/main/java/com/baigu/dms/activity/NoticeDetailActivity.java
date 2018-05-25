package com.baigu.dms.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ObservableScrollView;
import com.baigu.dms.domain.model.Notice;
import com.baigu.dms.presenter.NoticePresenter;
import com.baigu.dms.presenter.impl.NoticePresenterImpl;

import java.util.List;

/**
 * @Description 提现申请
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class NoticeDetailActivity extends BaseActivity implements NoticePresenter.NoticeView {

    private WebView mWebView;

    private NoticePresenter mNoticePresenter;
    private Notice mNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        initToolBar();
        mNotice = getIntent().getParcelableExtra("notice");
        if (mNotice == null) {
            finish();
            return;
        }
        setTitle(mNotice.getBtitle());
        initView();
        mNoticePresenter = new NoticePresenterImpl(this, this);
        mNoticePresenter.getNotice(mNotice.getIds());
    }

    private void initView() {
        mWebView = findView(R.id.webView);
        ViewUtils.setupWebViewSettings(mWebView.getSettings());
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
    public void onGetNoticeList(List<Notice> list) {

    }

    @Override
    public void onGetNotice(Notice notice) {
        if (notice != null) {
            mWebView.loadData(StringUtils.decodeHtmlString(notice.getContent()), "text/html; charset=UTF-8", null);
        }
    }
}
