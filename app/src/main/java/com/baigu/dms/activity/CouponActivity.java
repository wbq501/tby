package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.adapter.CouponAdapter;
import com.baigu.dms.adapter.WithdrawAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.domain.model.Coupon;
import com.baigu.dms.presenter.CouponPresenter;
import com.baigu.dms.presenter.impl.CouponPresenterimpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;
import com.hyphenate.chat.ChatClient;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CouponActivity extends BaseActivity implements OnLoadMoreListener,OnRefreshListener,OnItemClickListener,CouponPresenterimpl.CouponView {

    private LRecyclerView rv_hb;

    private CouponAdapter couponAdapter;

    CouponPresenter couponPresenter;

    private int pageNum = 1;
    private int mStatus = Coupon.Status.NO_USE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developing);
        initToolBar();
        setTitle(R.string.coupon);
        couponPresenter = new CouponPresenterimpl(this,this);
        initView();
        loadData();
    }

    private void initView() {
        rv_hb = findView(R.id.rv_hb);
        rv_hb.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        rv_hb.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_hb.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_hb.setLayoutManager(new LinearLayoutManager(this));
        couponAdapter = new CouponAdapter(this,couponPresenter,false);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(couponAdapter);
        rv_hb.setAdapter(adapter);
        rv_hb.setPullRefreshEnabled(true);
        rv_hb.setOnRefreshListener(this);
        rv_hb.setLoadMoreEnabled(true);
        rv_hb.setOnLoadMoreListener(this);

        addTab();
        EmptyViewUtil.initData(this,R.string.coupon_msg);
    }

    private void addTab() {
        TabLayout tabLayout = findView(R.id.tabLayout);

        //获取需要选中Tab的position
        int selPosition = 0;
        switch (mStatus) {
            case Coupon.Status.NO_USE:
                selPosition = 0;
                break;
            case Coupon.Status.YES_USE:
                selPosition = 1;
                break;
            case Coupon.Status.BE_OVERDUE:
                selPosition = 2;
                break;
            default:
                break;
        }

        //添加Tab
        TabLayout.Tab tab = tabLayout.newTab().setText(R.string.no_use);
        tabLayout.addTab(tab);
        if (selPosition == 0) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.yes_use);
        tabLayout.addTab(tab);
        if (selPosition == 1) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.be_overdue);
        tabLayout.addTab(tab);
        if (selPosition == 2) {
            tab.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        mStatus = Coupon.Status.NO_USE;
                        break;
                    case 1:
                        mStatus = Coupon.Status.YES_USE;
                        break;
                    case 2:
                        mStatus = Coupon.Status.BE_OVERDUE;
                        break;
                }
                pageNum = 1;
                couponAdapter.setState(mStatus);
                rv_hb.setNoMore(false);
                EmptyViewUtil.hide(CouponActivity.this);
                loadData();
//                couponPresenter.loadOrderList(mStatus, mCurrPage, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                couponAdapter.setData(null);
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        rv_hb.refreshComplete(10);
        loadData();
    }

    private void loadData(){
        couponPresenter.getCouponList(pageNum,mStatus);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void CouponList(Coupon coupon) {
        if (coupon == null && pageNum == 1){
            EmptyViewUtil.show(this);
            return;
        }
        if (coupon == null){
            EmptyViewUtil.hide(this);
            rv_hb.setNoMore(true);
            return;
        }
        List<Coupon.ListBean> lists = coupon.getList();
        if (lists.get(0).getCoupon() == null){
            EmptyViewUtil.show(this);
            rv_hb.setNoMore(true);
            return;
        }
        if (pageNum == 1){
            EmptyViewUtil.hide(this);
            if (lists == null || lists.size() == 0){
                EmptyViewUtil.show(this);
            }
            rv_hb.setNoMore(false);
        }else {
            rv_hb.setNoMore(false);
        }
        if (pageNum == 1){
            couponAdapter.clearData();
            couponAdapter.notifyDataSetChanged();
        }
        couponAdapter.appendDataList(lists);
        couponAdapter.notifyDataSetChanged();
        pageNum++;
    }
}
