package com.baigu.dms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BrandQuestionDetailActivity;
import com.baigu.dms.activity.BrandStoryActivity;
import com.baigu.dms.activity.SettingActivity;
import com.baigu.dms.adapter.BrandQuestionAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.MyFuncView;
import com.baigu.dms.common.view.MyInfoHeaderView;
import com.baigu.dms.common.view.MyOrderView;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.BrandQuestionPresenter;
import com.baigu.dms.presenter.MyDataPresenter;
import com.baigu.dms.presenter.UserPresenter;
import com.baigu.dms.presenter.impl.BrandQuestionPresenterImpl;
import com.baigu.dms.presenter.impl.MyDataPresenterImpl;
import com.baigu.dms.presenter.impl.UserPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.micky.logger.Logger;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 9:54
 */
public class MyFragment extends TabFragment implements View.OnClickListener, OnItemClickListener, OnRefreshListener, OnLoadMoreListener, MyDataPresenter.MyDataView, BrandQuestionPresenter.BrandQuestionView, UserPresenter.UserView {
    private LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private BrandQuestionAdapter mBrandQuestionAdapter;
    private MyInfoHeaderView mUserInfoHeaderView = null;
    private MyFuncView mMyFuncView;

    private int mPageSize = 10;
    private int mCurrPage = 2;

    private MyDataPresenter mMyDataPresenter;
    private BrandQuestionPresenter mBrandQuestionPresenter;
    private UserPresenter mUserPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        RxBus.getDefault().register(this);
        mMyDataPresenter = new MyDataPresenterImpl(getActivity(), this);
        mBrandQuestionPresenter = new BrandQuestionPresenterImpl(getActivity(), this);
        mUserPresenter = new UserPresenterImpl(getActivity(), this);
        onRefresh();
//        loadData();
        loadMessageNum();
        return view;
    }

    private void initView(View view) {
        findView(view, R.id.ll_setting).setOnClickListener(this);
        mRecyclerView = findView(view, R.id.iRecyclerView);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut);
        mRecyclerView.setArrowImageView(R.mipmap.refresh_down_arrow);
        mRecyclerView.setHeaderViewColor(R.color.white, R.color.white, R.color.colorPrimary);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.addItemDecoration(new HorizontalItemDecoration(getActivity()));

        mBrandQuestionAdapter = new BrandQuestionAdapter(getActivity());

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBrandQuestionAdapter);
        mLRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mUserInfoHeaderView = new MyInfoHeaderView(getActivity());

        mLRecyclerViewAdapter.addHeaderView(mUserInfoHeaderView);
        mLRecyclerViewAdapter.addHeaderView(new MyOrderView(getActivity()));
        mMyFuncView = new MyFuncView(getActivity());
        mLRecyclerViewAdapter.addHeaderView(mMyFuncView);

        View viewBrandQuetionTitle = LayoutInflater.from(getActivity()).inflate(R.layout.view_brand_question, null);
        TextView tvBrandQuestion = (TextView) viewBrandQuetionTitle.findViewById(R.id.tv_question_content);
        tvBrandQuestion.getLayoutParams().width = ViewUtils.getScreenInfo(getActivity()).widthPixels;
        mLRecyclerViewAdapter.addHeaderView(viewBrandQuetionTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserInfoHeaderView != null) {
            mUserInfoHeaderView.refreshData();
        }
    }

    @Override
    public void onTabChecked(boolean checked) {
        super.onTabChecked(checked);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        BrandQuestion brandQuestion = mBrandQuestionAdapter.getItem(position);
        if (brandQuestion != null) {
            Intent intent = new Intent(getActivity(), BrandQuestionDetailActivity.class);
            intent.putExtra("questionId", brandQuestion.getId());
            startActivity(intent);
        }
    }

    private void loadData() {
        mUserPresenter.getMyInfo(false);
        mCurrPage = 2;
        mRecyclerView.setNoMore(false);
        mBrandQuestionAdapter.clearData();
        mBrandQuestionAdapter.notifyDataSetChanged();
        mMyDataPresenter.getMyData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {
        mBrandQuestionPresenter.getBrandQuestion(mCurrPage);
    }

    @Override
    public void onGetMyData(MyDataResult result) {
        mRecyclerView.refreshComplete(mPageSize);
        if (result == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            mBrandQuestionAdapter.setData(null);
            mRecyclerView.setNoMore(true);
            mMyFuncView.setData(null);
            return;
        } else {
            mBrandQuestionAdapter.setData(result.brandQuestionVos);
            mMyFuncView.setData(result.advertisVo);
            mRecyclerView.setNoMore(result.brandQuestionVos.size() < 10);
        }
    }

    @Override
    public void onGetBrandQuestion(PageResult<BrandQuestion> pageResult) {
        mRecyclerView.setNoMore(pageResult != null && pageResult.lastPage);
        if (pageResult == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }
        if (pageResult.list != null) {
            mBrandQuestionAdapter.appendDataList(pageResult.list);
            mBrandQuestionAdapter.notifyDataSetChanged();
            mCurrPage++;
        }
    }

    @Override
    public void onGetBrandQuestionDetail(BrandQuestion question) {

    }

    @Override
    public void onGetMyInfo(User user) {
        if (mUserInfoHeaderView != null) {
            mUserInfoHeaderView.refreshData();
        }
    }

    @Override
    public void onLoadToken(String token) {

    }

    @Override
    public void onSaveHead(boolean result) {

    }

    private void loadMessageNum() {
        try {
            Conversation conversation = ChatClient.getInstance().chatManager().getConversation(getString(R.string.hx_customer));
            if (conversation == null) {
                return;
            }
            int count = conversation.unreadMessagesCount();
            if (mMyFuncView != null) {
                mMyFuncView.setMessageNum(count);
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        if (event.what == EventType.TYPE_UPDATE_HEAD_IMAGE) {
            mUserInfoHeaderView.onUpdateHead();
        } else if (event.what == EventType.TYPE_UPDATE_MESSAGE_NUM) {
            loadMessageNum();
        }
    }

    @Override
    public void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mUserPresenter != null) {
            mUserPresenter.onDestroy();
        }
        if (mMyDataPresenter != null) {
            mMyDataPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
