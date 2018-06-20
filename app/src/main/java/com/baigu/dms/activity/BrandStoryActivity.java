package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.CommentPraiseAdapter;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.MJavascriptInterface;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.BrandStoryDetailView;
import com.baigu.dms.common.view.MyWebViewClient;
import com.baigu.dms.common.view.shinebutton.ShineButton;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.domain.model.Praise;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BrandStoryDetailPresenter;
import com.baigu.dms.presenter.impl.BrandStoryDetailPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description 品牌故事详情
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/4 22:07
 */
public class BrandStoryActivity extends BaseActivity implements BrandStoryDetailPresenter.BrandStoryDetailView {

    private WebView mWebView;
    private BrandStory mBrandStory;
    private TextView mTvDate;
    private TextView mTvTitle;
    private TextView mTvContent;
    private ImageView mIvIcon;

    private BrandStoryDetailPresenter mBrandStoryDetailPresenter;
    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_story);
        initToolBar();
        setTitle(R.string.brand_story);

        mBrandStory = getIntent().getParcelableExtra("brandStory");
        if (mBrandStory == null) {
            finish();
            return;
        }
        mBrandStoryDetailPresenter = new BrandStoryDetailPresenterImpl(this, this);
        initView();
        mBrandStoryDetailPresenter.loadBrandStoryById(mBrandStory.getIds());
    }

    private void initView() {
        imageUrls = new ArrayList<>();
        mTvContent = findViewById(R.id.tv_content);
        mTvDate = findViewById(R.id.tv_time);
        mTvTitle = findViewById(R.id.tv_username);
        mIvIcon = findViewById(R.id.iv_head);
        mWebView = findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
    }

    @Override
    public void onLoadBrandStory(BrandStory brandStory) {
        mBrandStory = brandStory;
        if (mBrandStory == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            finish();
            return;
        }
        mTvTitle.setText(brandStory.getBrand_title());
        mTvDate.setText(brandStory.getCreate_time());
       if(TextUtils.isEmpty(brandStory.getBrand_brief())){
           mTvContent.setText("暂无内容");
       }else{
           mTvContent.setText(brandStory.getBrand_brief());
       }
//        ImageUtil.loadImage(this,brandStory.getBrand_ctx_img(),mIvIcon);
        ImageUtil.loadImage(this,brandStory.getBrand_img(),mIvIcon);
        mWebView.loadDataWithBaseURL(null, brandStory.getBrand_content(), "text/html" , "utf-8", null);
        imageUrls = StringUtils.returnImageUrlsFromHtml(brandStory.getBrand_content());
        mWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls),"imagelistener");
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void onLoadCommentList(PageResult<Comment> pageResult) {

    }

    @Override
    public void onLoadPraiseList(PageResult<Praise> pageResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBrandStoryDetailPresenter != null) {
            mBrandStoryDetailPresenter.onDestroy();
        }
    }
}
