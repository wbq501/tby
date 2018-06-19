package com.baigu.dms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.activity.BrandStoryActivity;
import com.baigu.dms.activity.BrandStoryListActivity;
import com.baigu.dms.activity.ChatActivity;
import com.baigu.dms.activity.GoodsSearchActivity;
import com.baigu.dms.activity.WebActivity;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.BrandStoryAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.AdvertView;
import com.baigu.dms.common.view.BannerView;
import com.baigu.dms.common.view.NoticeView;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.RecommendClassView;
import com.baigu.dms.common.view.StarGoodsView;
import com.baigu.dms.domain.db.DBCore;
import com.baigu.dms.domain.db.dao.DaoSession;
import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.common.model.HomeResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.HomePresenter;
import com.baigu.dms.presenter.LoginPresenter;
import com.baigu.dms.presenter.impl.HomePreseterImpl;
import com.baigu.dms.presenter.impl.LoginPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.micky.logger.Logger;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 9:54
 */
public class HomeFragment extends TabFragment implements OnRVItemClickListener, OnRefreshListener, OnLoadMoreListener, HomePresenter.HomeView, View.OnClickListener, LoginPresenter.LoginView {

    private View mllToolbar;
    private LRecyclerView mRecyclerView;
    private View mLayoutSearch;
    private AdvertView mAdView;//
    private AdvertView mCompany;//合作伙伴
    private View mCompanyHeader;
    private NoticeView mNoticeView;//tips
    private RecommendClassView mRecommendView;//分类
    private StarGoodsView mStarGoodsView;// 今日推荐
    private View mBrandStoryHeader;//最新消息
    private BannerView mBannerView;
    private View mRootView;

    private int mToolbarBgAlpha;
    private int mPageSize = 10;
    private int mCurrPage = 2;

    private List<BrandStory> brandStoryList;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private BrandStoryAdapter mBrandStroyAdapter;
    private HomePresenter mHomePresenter;

    private TextView mTvMessageNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        }
        ViewGroup mViewGroup = (ViewGroup) mRootView.getParent();
        if (mViewGroup != null) {
            mViewGroup.removeView(mRootView);
        }
        mllToolbar = findView(mRootView, R.id.ll_toolbar);
        mllToolbar.getBackground().mutate().setAlpha(255);
        mHomePresenter = new HomePreseterImpl((BaseActivity) getActivity(), this);
        initView(mRootView);
        RxBus.getDefault().register(this);
        mllToolbar.setVisibility(View.GONE);
        String token = TokenManager.getInstance().getToken();

        loadData(true);
        loadMessageNum();
        return mRootView;
    }

    public void setMessageNum(int num) {
        if (num <= 0) {
            mTvMessageNum.setVisibility(View.GONE);
        } else if (num >= 99) {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(num + "+");
        } else {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(String.valueOf(num));
        }
    }

    private void loadMessageNum() {
        try {
            Conversation conversation = ChatClient.getInstance().chatManager().getConversation(getString(R.string.hx_customer));
            if (conversation == null) {
                return;
            }
            int count = conversation.unreadMessagesCount();
            setMessageNum(count);
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    private void initView(View view) {
        mLayoutSearch = findView(view, R.id.layout_search);
        mLayoutSearch.setOnClickListener(this);

        findView(view, R.id.ll_custom_service).setOnClickListener(this);
        mTvMessageNum = findView(view,R.id.tv_message_num);

        mRecyclerView = findView(view, R.id.iRecyclerView);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScalePulseOut);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRecyclerView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.white);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.addItemDecoration(new HorizontalItemDecoration(getActivity()));

        mBrandStroyAdapter = new BrandStoryAdapter(getActivity());
        mBrandStroyAdapter.setOnItemClickListener(this);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBrandStroyAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setPullRefreshEnabled(true);
//        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
//        mRecyclerView.setOnLoadMoreListener(this);

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int i, int i1) {
                if (mllToolbar == null) return;
                float height = (int) (ViewUtils.getScreenInfo(getActivity()).widthPixels * 0.5);  //获取图片的高度
                if (i1 < height) {
                    mToolbarBgAlpha = Float.valueOf(i1 / height * 255).intValue();    //i 有可能小于 0
                } else {
                    mToolbarBgAlpha = 255;
                }
                updateAlpha();
            }

            @Override
            public void onScrollStateChanged(int i) {

            }

            @Override
            public void onHeaderShowed(int height) {

            }
        });


        mAdView = new AdvertView(getContext());
        mAdView.setOnItemClickListener(new AdvertView.OnAdvertItemClickListener() {
            @Override
            public void onItemClick(Advert advert) {
                if (TextUtils.isEmpty(advert.getAdvertis_content())) {
                    return;
                }
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("title", advert.getAdvertis_title());
                intent.putExtra("content", advert.getAdvertis_content());
                startActivity(intent);
            }
        });
        mLRecyclerViewAdapter.addHeaderView(mAdView);
        mAdView.setVisibility(View.GONE);

        mNoticeView = new NoticeView(getContext());
        mLRecyclerViewAdapter.addHeaderView(mNoticeView);
        mNoticeView.setVisibility(View.GONE);

        mRecommendView = new RecommendClassView(getContext());
        mLRecyclerViewAdapter.addHeaderView(mRecommendView);
        mRecommendView.setVisibility(View.GONE);

        mStarGoodsView = new StarGoodsView(getContext());
        mLRecyclerViewAdapter.addHeaderView(mStarGoodsView);
        mStarGoodsView.setVisibility(View.GONE);

//        mCompanyHeader = LayoutInflater.from(getContext()).inflate(R.layout.view_comapany_head,null);
//        mLRecyclerViewAdapter.addHeaderView(mCompanyHeader);
//        mCompanyHeader.setVisibility(View.GONE);

//        mCompany = new AdvertView(getContext());
//        mCompany.setOnItemClickListener(new AdvertView.OnAdvertItemClickListener() {
//            @Override
//            public void onItemClick(Advert advert) {
//                if (TextUtils.isEmpty(advert.getAdvertis_content())) {
//                    return;
//                }
//                Intent intent = new Intent(getContext(), WebActivity.class);
//                intent.putExtra("title", advert.getAdvertis_title());
//                intent.putExtra("content", advert.getAdvertis_content());
//                startActivity(intent);
//            }
//        });
//        mLRecyclerViewAdapter.addHeaderView(mCompany);
//        mCompany.setVisibility(View.GONE);


        mBannerView = new BannerView(getContext());
        mBannerView.setOnItemClickListener(new BannerView.OnBannerItemClickListener() {
            @Override
            public void onItemClick(View view, Advert advert) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("title", advert.getAdvertis_title());
                if (TextUtils.isEmpty(advert.getAdvertis_content())) {
                    return;
                }
                intent.putExtra("content", advert.getAdvertis_content());
                startActivity(intent);
            }
        });
        mLRecyclerViewAdapter.addHeaderView(mBannerView);
        mBannerView.setVisibility(View.GONE);


        mBrandStoryHeader = LayoutInflater.from(getContext()).inflate(R.layout.item_brand_story_head, null);
        mBrandStoryHeader.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BrandStoryListActivity.class));
            }
        });
        mLRecyclerViewAdapter.addHeaderView(mBrandStoryHeader);
        mBrandStoryHeader.setVisibility(View.GONE);
    }

    private void loadData(boolean showProgress) {
        mCurrPage = 2;
        mRecyclerView.setNoMore(false);
        mBrandStroyAdapter.clearData();

        mBrandStroyAdapter.notifyDataSetChanged();
        mHomePresenter.loadHomeData(showProgress);
    }

    private void updateAlpha() {
        mllToolbar.getBackground().mutate().setAlpha(Math.max(mToolbarBgAlpha, 0));   // 0~255 透明度
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAlpha();
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.layout_search:
                startActivity(new Intent(getActivity(), GoodsSearchActivity.class));
                break;
            case R.id.ll_custom_service:
                startActivity(new Intent(getActivity(), ChatActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabChecked(boolean checked) {
        super.onTabChecked(checked);
        if (checked) {
            if (mllToolbar != null) {
                mllToolbar.getBackground().mutate().setAlpha(mToolbarBgAlpha);
            }
        } else {
            if (mllToolbar != null) {
                mllToolbar.getBackground().mutate().setAlpha(255);
            }
        }
    }

    @Override
    public void onLoadMore() {
        mHomePresenter.loadBrandStory(mCurrPage);
    }


    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
            Intent intent=new Intent(getContext(), BrandStoryActivity.class);
            intent.putExtra("brandStory",brandStoryList.get(position-6));
            getActivity().startActivity(intent);
    }


    @Override
    public void onRefresh() {
        loadData(false);
    }

    @Override
    public void onLoadHomeData(HomeResult homeData) {
        mllToolbar.setVisibility(View.VISIBLE);
        if (homeData == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            mBrandStroyAdapter.setData(null);
            mRecyclerView.refreshComplete(mPageSize);
            mBannerView.setData(null);
            return;
        }
        mAdView.setVisibility(View.VISIBLE);
//        mCompany.setVisibility(View.VISIBLE);
//        mCompanyHeader.setVisibility(View.VISIBLE);
        mNoticeView.setVisibility(View.VISIBLE);
        mStarGoodsView.setVisibility(View.VISIBLE);
        mBannerView.setVisibility(View.VISIBLE);
        mRecommendView.setVisibility(View.VISIBLE);
        mBrandStoryHeader.setVisibility(View.VISIBLE);
        if (mAdView != null) {
            mAdView.setData(homeData.bannerlist);
        }
//        if(mCompany != null){
//            mCompany.setData(homeData.bannerList);
//
//        }
        if (mNoticeView != null) {
            mNoticeView.setData(homeData.cbullitinlist);
        }
        if (mBannerView != null) {
            mBannerView.setData(homeData.advertislist);
        }
        if (mRecommendView != null) {
            mRecommendView.setData(homeData.recommendCategories);
        }
//        if(mCompany!=null){
//            //todo
//        }

        if (mStarGoodsView != null) {
            mStarGoodsView.setData(homeData.superGoodsList);
        }
        brandStoryList = homeData.brandList;
        mBrandStroyAdapter.setData(brandStoryList);
        mRecyclerView.refreshComplete(mPageSize);
    }

    @Override
    public void onLoadBrandStory(BaseResponse<List<BrandStory>> brandStoryResult) {
        mRecyclerView.setNoMore(brandStoryResult != null && brandStoryResult.getData().size() == 0);
        if (brandStoryResult.getCode() != 1) {
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }
        if (brandStoryResult.getData() != null) {
//            if (brandStoryResult.getData().size() == 0) {
//                ViewUtils.showToastError(R.string.empty_data);
//            } else {
            mBrandStroyAdapter.appendDataList(brandStoryResult.getData());
            mBrandStroyAdapter.notifyDataSetChanged();
            mCurrPage++;
//            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        switch (event.what) {
            case EventType.TYPE_COMMENT_ADDED:
                String brandId = event.object == null ? "" : event.object.toString();
                if (mBrandStroyAdapter != null && mBrandStroyAdapter.getDataList() != null && !TextUtils.isEmpty(brandId)) {
                    for (BrandStory brandStory : mBrandStroyAdapter.getDataList()) {
                        if (brandId.equals(brandStory.getIds())) {
                            brandStory.setSs(brandStory.getSs() + 1);
                            break;
                        }
                    }
                    mBrandStroyAdapter.notifyDataSetChanged();
                }
                break;
            case EventType.TYPE_PRAISE_ADDED:
                brandId = event.object == null ? "" : event.object.toString();
                if (mBrandStroyAdapter != null && mBrandStroyAdapter.getDataList() != null && !TextUtils.isEmpty(brandId)) {
                    for (BrandStory brandStory : mBrandStroyAdapter.getDataList()) {
                        if (brandId.equals(brandStory.getIds())) {
                            brandStory.setDd(brandStory.getDd() + 1);
                            brandStory.setIsdz(1);
                            break;
                        }
                    }
                }
                mBrandStroyAdapter.notifyDataSetChanged();
                break;
            case EventType.TYPE_UPDATE_MESSAGE_NUM:
                loadMessageNum();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mHomePresenter != null) {
            mHomePresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLogin(boolean result) {
        if(result){
            loadData(true);
        }
    }
}
