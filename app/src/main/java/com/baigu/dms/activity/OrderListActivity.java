package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.adapter.OrderAdapter;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.EmptyViewUtil;
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
import com.baigu.dms.presenter.impl.OrderPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单列表
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class OrderListActivity extends BaseActivity implements OnLoadMoreListener, OrderPresenter.OrderView, OnItemClickListener {

    private LRecyclerView mRvOrder;

    private OrderAdapter mOrderAdapter;
    private OrderPresenter mOrderPresenter;

    private int mCurrPage = 1;
    private int mStatus = Order.Status.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        initToolBar();
        setTitle(R.string.my_oder);

        mStatus = getIntent().getIntExtra("status", Order.Status.ALL);

        mOrderPresenter = new OrderPresenterImpl(this, this);

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
        mOrderAdapter = new OrderAdapter(this, mOrderPresenter);
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
            case Order.Status.ALL:
                selPosition = 0;
                break;
            case Order.Status.UNPAY:
                selPosition = 1;
                break;
            case Order.Status.UNDELIVER:
                selPosition = 2;
                break;
            case Order.Status.DELIVERED:
                selPosition = 3;
                break;
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
        TabLayout.Tab tab = tabLayout.newTab().setText(R.string.all);
        tabLayout.addTab(tab);
        if (selPosition == 0) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.unpay);
        tabLayout.addTab(tab);
        if (selPosition == 1) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.undeliver);
        tabLayout.addTab(tab);
        if (selPosition == 2) {
            tab.select();
        }

        tab = tabLayout.newTab().setText(R.string.delivered);
        tabLayout.addTab(tab);
        if (selPosition == 3) {
            tab.select();
        }

//        tab = tabLayout.newTab().setText(R.string.refund);
//        tabLayout.addTab(tab);
//        if (selPosition == 4) {
//            tab.select();
//        }

//        tab = tabLayout.newTab().setText(R.string.preparing);
//        tabLayout.addTab(tab);
//        if (selPosition == 4) {
//            tab.select();
//        }



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        mStatus = Order.Status.ALL;
                        break;
                    case 1:
                        mStatus = Order.Status.UNPAY;
                        break;
                    case 2:
                        mStatus = Order.Status.UNDELIVER;
                        break;
                    case 3:
                        mStatus = Order.Status.DELIVERED;
                        break;
//                    case 4:
//                        mStatus = Order.Status.PREPARING;
//                        break;
//                    case 5:
//                        mStatus = Order.Status.DELIVERED;
//                        break;
//                    default:
//                        mStatus = Order.Status.ALL;
//                        break;
                }
                mCurrPage = 1;
                mRvOrder.setNoMore(false);
                EmptyViewUtil.hide(OrderListActivity.this);
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
        EmptyViewUtil.hide(OrderListActivity.this);
        mOrderPresenter.loadOrderList(mStatus, mCurrPage, true);
    }

    @Override
    public void onLoadOrderList(PageResult<Order> orderPageResult) {
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
            List<OrderAdapter.OrderViewItem> orderViewItemList = new ArrayList<>();
            for (Order order : orderPageResult.list) {
                switch (order.getStatus()){
                    case  0:
                        order.setStatus(Order.Status.UNPAY);
                        break;
                    case  10:
                        order.setStatus(Order.Status.UNDELIVER);
                        break;
//                    case  20:
//                        order.setStatus(Order.Status.U);
//                        break;
                    case  20:
                        order.setStatus(Order.Status.DELIVERED);
                        break;
                }
                orderViewItemList.add(new OrderAdapter.OrderViewItem(order, OrderAdapter.OrderViewItemType.TOP));
                int goodsNum = 0;
                if (order.getGoodsList() != null && order.getGoodsList().size() > 0) {
                    int i = 0;
                    for (OrderGoods orderGoods : order.getGoodsList()) {
                        goodsNum += orderGoods.getGoodsNum();
                        orderViewItemList.add(new OrderAdapter.OrderViewItem(order, orderGoods, OrderAdapter.OrderViewItemType.GOODS));
                        orderGoods.setIndex(i);
                        i++;
                    }
                }
                order.setGoodsNum(goodsNum);
                orderViewItemList.add(new OrderAdapter.OrderViewItem(order, OrderAdapter.OrderViewItemType.BOTTOM));
            }
            mOrderAdapter.appendDataList(orderViewItemList);
            mOrderAdapter.notifyDataSetChanged();
            mCurrPage++;
        }
        if (mOrderAdapter.getItemCount() <= 0) {
            EmptyViewUtil.show(this);
        }
    }

    @Override
    public void onRefundOrder(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_refund_order);
            RxBus.getDefault().post(EventType.TYPE_REFUND_ORDER);
        } else {
            ViewUtils.showToastError(R.string.failed_refund_order);
        }
    }

    @Override
    public void onCancelOrder(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_cancel_order);
            refresh();
        } else {
            ViewUtils.showToastError(R.string.failed_cancel_order);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        OrderAdapter.OrderViewItem item = mOrderAdapter.getItem(position);
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
}