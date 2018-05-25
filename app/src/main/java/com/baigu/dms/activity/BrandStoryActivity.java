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
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.BrandStoryDetailView;
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

//    private static final int REQUEST_CODE_COMMENT = 40001;
//    private LRecyclerView mRecyclerView;
//    private ShineButton mShineButton;
//
//
//    private LRecyclerViewAdapter mRecyclerViewAddapter;
//    private CommentPraiseAdapter mCommentPraiseAdapter;
//    private BrandStoryDetailView mCommentDetailView;

    private WebView mWebView;
    private BrandStory mBrandStory;
    private TextView mTvDate;
    private TextView mTvTitle;
    private TextView mTvContent;
    private ImageView mIvIcon;


//    private int mCurrCommentPage = 1;
//    private int mCurrPraisePage = 1;
//
//    private boolean mCommentTab = true;
//    private boolean mCommentNoMore;
//    private boolean mPraiseNoMore;

    private BrandStoryDetailPresenter mBrandStoryDetailPresenter;

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
//        mBrandStoryDetailPresenter.loadCommentList(mBrandStory.getId(), mCurrCommentPage, false);
//        mBrandStoryDetailPresenter.loadPraiseList(mBrandStory.getId(), mCurrPraisePage, false);
    }

    private void initView() {
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
//        mRecyclerView = findView(R.id.rv_comment_praise);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
//        mRecyclerView.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
//        mRecyclerView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mCommentPraiseAdapter = new CommentPraiseAdapter(this);
//        mRecyclerViewAddapter = new LRecyclerViewAdapter(mCommentPraiseAdapter);
//        mRecyclerView.setAdapter(mRecyclerViewAddapter);
//        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.setLoadMoreEnabled(true);
//        mRecyclerView.setOnLoadMoreListener(this);
//
//        mCommentDetailView = new BrandStoryDetailView(this);
//        mCommentDetailView.setBrandStory(mBrandStory);
//        mCommentDetailView.setOnCommentPraiseTabChangeListener(this);
//        mRecyclerViewAddapter.addHeaderView(mCommentDetailView);
//
//        mShineButton = (ShineButton) findViewById(R.id.btn_praise);
//        mShineButton.setChecked(mBrandStory.getIsdz() > 0);
//        findViewById(R.id.layout_praise).setOnClickListener(new OnPraiseClickListener(mShineButton));
//        findViewById(R.id.layout_comment).setOnClickListener(new OnCommentClickListener());
    }


//    @Override
//    public void onLoadCommentList(PageResult<Comment> pageResult) {
//        mRecyclerView.setNoMore(pageResult != null && pageResult.lastPage);
//        if (pageResult == null) {
//            if (mCurrCommentPage == 1) {
//                mCommentPraiseAdapter.clearCommentData();
//                mCommentPraiseAdapter.notifyDataSetChanged();
//            }
//            ViewUtils.showToastError(R.string.failed_load_data);
//            return;
//        }
//        if (pageResult.list == null && mCurrCommentPage == 1) {
//            mCommentPraiseAdapter.clearCommentData();
//            mCommentPraiseAdapter.notifyDataSetChanged();
//        }
//        if (pageResult.list != null) {
//            if (mCurrCommentPage == 1) {
//                mCommentPraiseAdapter.setCommentData(pageResult.list);
//            } else {
//                mCommentPraiseAdapter.appendCommentDataList(pageResult.list);
//            }
//            mCommentPraiseAdapter.notifyDataSetChanged();
//            if (pageResult.lastPage) {
//                mCommentNoMore = true;
//            }
//            mCurrCommentPage++;
//        }
//    }

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
        ImageUtil.loadImage(this,brandStory.getBrand_img(),mIvIcon);
        mWebView.loadDataWithBaseURL(null, brandStory.getBrand_content(), "text/html" , "utf-8", null);
        //        mShineButton.setChecked(mBrandStory.getIsdz() > 0);
//        mCommentDetailView.setBrandStory(mBrandStory);
    }

    @Override
    public void onLoadCommentList(PageResult<Comment> pageResult) {

    }

    @Override
    public void onLoadPraiseList(PageResult<Praise> pageResult) {

    }

//    @Override
//    public void onLoadPraiseList(PageResult<Praise> pageResult) {
//        mRecyclerView.setNoMore(pageResult != null && pageResult.lastPage);
//        if (pageResult == null) {
//            if (mCurrPraisePage == 1) {
//                mCommentPraiseAdapter.clearPraiseData();
//                mCommentPraiseAdapter.notifyDataSetChanged();
//            }
//            ViewUtils.showToastError(R.string.failed_load_data);
//            return;
//        }
//        if (pageResult.list == null && mCurrPraisePage == 1) {
//            mCommentPraiseAdapter.clearPraiseData();
//            mCommentPraiseAdapter.notifyDataSetChanged();
//        }
//        if (pageResult.list != null) {
//            if (mCurrPraisePage == 1) {
//                mCommentPraiseAdapter.setPraiseData(pageResult.list);
//            } else {
//                mCommentPraiseAdapter.appendPraiseDataList(pageResult.list);
//            }
//            mCommentPraiseAdapter.notifyDataSetChanged();
//            if (pageResult.lastPage) {
//                mPraiseNoMore = true;
//            }
//            mCurrPraisePage++;
//        }
//    }

//    @Override
//    public void onCommentPraiseTabSelect(boolean commentTab) {
//        mCommentTab = commentTab;
//        mCommentPraiseAdapter.selCommentTab(commentTab);
//        mRecyclerView.setNoMore(commentTab ? mCommentNoMore : mPraiseNoMore);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode ==  REQUEST_CODE_COMMENT) {
//            Comment comment = data.getParcelableExtra("comment");
//            mCommentPraiseAdapter.addComment(comment);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBrandStoryDetailPresenter != null) {
            mBrandStoryDetailPresenter.onDestroy();
        }
    }

//    class OnPraiseClickListener implements View.OnClickListener {
//
//        private ShineButton shineButton;
//
//        public OnPraiseClickListener(ShineButton shineButton) {
//            this.shineButton = shineButton;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (mBrandStory.getIsdz() > 0) {
//                return;
//            }
//            if (!(v instanceof ShineButton)) {
//                shineButton.click();
//            }
//            ServiceManager.createGsonService(BrandStoryService.class)
//                    .addPraise(UserCache.getInstance().getUser().getIds(), mBrandStory.getId())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<BaseResponse>() {
//                        @Override
//                        public void accept(BaseResponse response) throws Exception {
//                            if (response != null && BaseResponse.SUCCESS.equals(response.getStatus())) {
//                                mBrandStory.setDd(mBrandStory.getDd() + 1);
//                                mBrandStory.setIsdz(1);
//                                RxBus.getDefault().post(EventType.TYPE_PRAISE_ADDED, mBrandStory.getId());
//                            } else {
//                                ViewUtils.showToastError(R.string.failed_add_praise);
//                            }
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(@NonNull Throwable throwable) throws Exception {
//                            ViewUtils.showToastError(R.string.failed_add_praise);
//                        }
//                    });
//        }
//    }
//
//    class OnCommentClickListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            if (ViewUtils.isFastClick()) return;
//            Intent intent = new Intent(BrandStoryActivity.this, BrandCommentAddActivity.class);
//            intent.putExtra("brandId", mBrandStory.getId());
//            startActivityForResult(intent, REQUEST_CODE_COMMENT);
//        }
//    }
}
