package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.adapter.OrderAdapter;
import com.baigu.dms.adapter.OrderRepealAdapter;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.OrderUtils;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.model.OrderGoods;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.OrderPresenter;
import com.baigu.dms.presenter.OrderRepealPresenter;
import com.baigu.dms.presenter.impl.OrderPresenterImpl;
import com.baigu.dms.presenter.impl.OrderRepealPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 撤销订单
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class OrderRepealListActivity extends BaseActivity implements OnLoadMoreListener, OrderRepealPresenter.OrderRepealView, OnItemClickListener {

    private LRecyclerView mRvOrder;

    private OrderRepealAdapter mOrderAdapter;
    private OrderRepealPresenter mOrderPresenter;

    private int mCurrPage = 1;
    private int mStatus = Order.Status.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initToolBar();
        setTitle(R.string.refund);

        mStatus = getIntent().getIntExtra("status", OrderUtils.REFUND_APPLY_FOR);

        mOrderPresenter = new OrderRepealPresenterImpl(this, this);

        initView();
        mOrderPresenter.loadOrderList(mStatus, mCurrPage, true);
        RxBus.getDefault().register(this);
    }

    private void initView() {
        mRvOrder = findView(R.id.rv_order);
        mRvOrder.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRvOrder.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvOrder.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderRepealAdapter(this, mOrderPresenter);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mOrderAdapter);
        adapter.setOnItemClickListener(this);
        mRvOrder.setAdapter(adapter);
        mRvOrder.setPullRefreshEnabled(false);
        mRvOrder.setLoadMoreEnabled(true);
        mRvOrder.setOnLoadMoreListener(this);

        addTab();
        EmptyViewUtil.initData(this, R.string.order_list_empty);
    }

    private void addTab() {
        TabLayout tabLayout = findView(R.id.tabLayout);

        //获取需要选中Tab的position
        int selPosition = 0;
        switch (mStatus) {
            case OrderUtils.REFUND_APPLY_FOR:
                selPosition = 0;
                break;
            case OrderUtils.REFUND_APPLY:
                selPosition = 1;
                break;
            case OrderUtils.REFUNDED:
                selPosition = 2;
                break;
//            case Order.Status.DELIVERED:
//                selPosition = 3;
//                break;
//            case Order.Status.PREPARING:
//                selPosition = 4;
//                break;
//            case Order.Status.DELIVERED:
//                selPosition = 5;
//                break;
            default:
                break;
        }

        //添加Tab
        TabLayout.Tab tab = tabLayout.newTab().setText(R.string.refund_apply);
        tabLayout.addTab(tab);
        if (selPosition == 0) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.refund_applyed);
        tabLayout.addTab(tab);
        if (selPosition == 1) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.refunded);
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
                        mStatus = OrderUtils.REFUND_APPLY_FOR;
                        break;
                    case 1:
                        mStatus = OrderUtils.REFUND_APPLY;
                        break;
                    case 2:
                        mStatus = OrderUtils.REFUNDED;
                        break;

                    default:

                        break;
                }
                mCurrPage = 1;
                mRvOrder.setNoMore(false);
                EmptyViewUtil.hide(OrderRepealListActivity.this);
                mOrderPresenter.loadOrderList(mStatus, mCurrPage, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mOrderAdapter.setData(null);
                mOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void refresh() {
        mCurrPage = 1;
        mRvOrder.setNoMore(false);
        mOrderPresenter.loadOrderList(mStatus, mCurrPage, true);

    }

    @Override
    public void onLoadMore() {
        EmptyViewUtil.hide(OrderRepealListActivity.this);
        mOrderPresenter.loadOrderList(mStatus, mCurrPage, true);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        OrderRepealAdapter.OrderViewItem item = mOrderAdapter.getItem(position);
        if (item != null && item.order != null) {
            intent.putExtra("orderId", item.order.getId());
            intent.putExtra("orderDate", StringUtils.getOrderDate(DateUtils.longToStr(Long.valueOf(item.order.getCreateTime()),new SimpleDateFormat("yyyyMMdd"))));
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        if (event.what == EventType.TYPE_CANCEL_ORDER || event.what == EventType.TYPE_REFUND_ORDER) {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mOrderPresenter != null) {
            mOrderPresenter.onDestroy();
        }
        if (mOrderAdapter != null) {
            mOrderAdapter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void loadOrderList(PageResult<Order> orderPageResult) {
        mRvOrder.setNoMore(orderPageResult != null && orderPageResult.lastPage);
        if (mCurrPage == 1) {
            mOrderAdapter.clearData();
            mOrderAdapter.notifyDataSetChanged();
        }
        if (orderPageResult == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }
        if (orderPageResult.list != null) {
            List<OrderRepealAdapter.OrderViewItem> orderViewItemList = new ArrayList<>();
            for (Order order : orderPageResult.list) {

                orderViewItemList.add(new OrderRepealAdapter.OrderViewItem(order, OrderRepealAdapter.OrderViewItemType.TOP));
                int goodsNum = 0;
                if (order.getGoodsList() != null && order.getGoodsList().size() > 0) {
                    int i = 0;
                    for (OrderGoods orderGoods : order.getGoodsList()) {
                        goodsNum += orderGoods.getGoodsNum();
                        orderViewItemList.add(new OrderRepealAdapter.OrderViewItem(order, orderGoods, OrderRepealAdapter.OrderViewItemType.GOODS));
                        orderGoods.setIndex(i);
                        i++;
                    }
                }
                order.setGoodsNum(goodsNum);
                orderViewItemList.add(new OrderRepealAdapter.OrderViewItem(order, OrderRepealAdapter.OrderViewItemType.BOTTOM));
            }
            mOrderAdapter.appendDataList(orderViewItemList);
            mOrderAdapter.notifyDataSetChanged();
            mCurrPage++;
        }
        if (mOrderAdapter.getItemCount() <= 0) {
            EmptyViewUtil.show(this);
        }
    }
}