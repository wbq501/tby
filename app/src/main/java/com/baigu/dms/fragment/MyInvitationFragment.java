package com.baigu.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baigu.dms.R;
import com.baigu.dms.adapter.InvitationAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.InvitationPresenter;
import com.baigu.dms.presenter.impl.InvitationPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

/**
 * @Description 我的邀请
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/4 22:43
 */
public class MyInvitationFragment extends BaseFragment implements InvitationPresenter.InvitaionView, OnLoadMoreListener {
    private LRecyclerView mRvInvitation;
    private InvitationAdapter mInvitationAdapter;
    private boolean mUnVerify;

    private int mCurrPage = 1;
    private InvitationPresenter mInvitationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        mInvitationPresenter = new InvitationPresenterImpl(getActivity(), this);
        initView(view);
        RxBus.getDefault().register(this);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 100);
        return view;
    }

    private void loadData() {
        mCurrPage = 1;
        mRvInvitation.setNoMore(false);
        if (mUnVerify) {
            mInvitationPresenter.loadInvitationUnVerify(mCurrPage, true);
        } else {
            mInvitationPresenter.loadInvitationVerified(mCurrPage, true);
        }
    }

    public void setUnVerfy(boolean b) {
        mUnVerify = b;
    }

    private void initView(View view) {
        mRvInvitation = findView(view, R.id.rv_invitation);
        mRvInvitation.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRvInvitation.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvInvitation.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvInvitation.setLayoutManager(new LinearLayoutManager(getContext()));
        mInvitationAdapter = new InvitationAdapter(getActivity(), mInvitationPresenter);
        mInvitationAdapter.setUnVerified(mUnVerify);
        mRvInvitation.setAdapter(new LRecyclerViewAdapter(mInvitationAdapter));
        mRvInvitation.setPullRefreshEnabled(false);
        mRvInvitation.setLoadMoreEnabled(true);
        mRvInvitation.setOnLoadMoreListener(this);
        EmptyViewUtil.initData(view, R.string.data_empty);
    }

    @Override
    public void onLoadMore() {
        EmptyViewUtil.hide(getView());
        if (mUnVerify) {
            mInvitationPresenter.loadInvitationUnVerify(mCurrPage, false);
        } else {
            mInvitationPresenter.loadInvitationVerified(mCurrPage, false);
        }
    }

    @Override
    public void onLoadInvitationUnVerify(PageResult<User> pageResult) {
        setupData(pageResult);
    }

    @Override
    public void onLoadInvitationVerified(PageResult<User> pageResult) {
        setupData(pageResult);
    }

    private void setupData(PageResult<User> pageResult) {
        mRvInvitation.setNoMore(pageResult != null && pageResult.lastPage);
        if (pageResult == null) {
            if (mCurrPage == 1) {
                mInvitationAdapter.clearData();
                mInvitationAdapter.notifyDataSetChanged();
            }
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }
        if (pageResult.list == null && mCurrPage == 1) {
            mInvitationAdapter.clearData();
            mInvitationAdapter.notifyDataSetChanged();
        }
        if (pageResult.list != null) {
            if (mCurrPage == 1) {
                mInvitationAdapter.setData(pageResult.list);
            } else {
                mInvitationAdapter.appendDataList(pageResult.list);
            }
            mInvitationAdapter.notifyDataSetChanged();
            mCurrPage++;
        }
        if (mInvitationAdapter.getItemCount() <= 0) {
            EmptyViewUtil.show(getView());
        }
    }

    @Override
    public void onVerify(boolean result) {
        if (result) {
            RxBus.getDefault().post(EventType.TYPE_VERIFY_INVITATION);
            ViewUtils.showToastSuccess(R.string.success_verify);
        } else {
            ViewUtils.showToastError(R.string.failed_verify);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        if (event.what == EventType.TYPE_VERIFY_INVITATION) {
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mInvitationPresenter != null) {
            mInvitationPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
