package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.PayActivity;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.OrderUtils;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.model.OrderGoods;
import com.baigu.dms.presenter.OrderPresenter;
import com.baigu.dms.presenter.OrderRepealPresenter;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

public class OrderRepealAdapter extends BaseRVAdapter<OrderRepealAdapter.OrderViewItem> {
    public Activity mActivity;
    private OrderRepealPresenter mOrderPresenter;
//    private ConfirmDialog mCancelConfirmDialog;
//    private ConfirmDialog mRefundConfirmDialog;

    public OrderRepealAdapter(Activity activity, OrderRepealPresenter presenter) {
        mActivity = activity;
        mOrderPresenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        OrderViewItem item = getItem(position);
        return item.type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case OrderViewItemType.TOP:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_top, parent, false);
                holder = new TopHolder(view);
                break;
            case OrderViewItemType.GOODS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_goods, parent, false);
                holder = new GoodsHolder(view);
                break;
            case OrderViewItemType.BOTTOM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_bottom, parent, false);
                holder = new BottomHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.bindData(getItem(position));
    }

    class TopHolder extends BaseViewHolder {
        TextView tvOrderNo;
        TextView tvStatus;
        TextView tvTakeUser;
        TextView tvTakePhone;

        public TopHolder(View itemView) {
            super(itemView);
            tvOrderNo = (TextView) itemView.findViewById(R.id.tv_order_no);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvTakeUser = (TextView) itemView.findViewById(R.id.tv_take_user);
            tvTakePhone = (TextView) itemView.findViewById(R.id.tv_take_phone);
        }

        @Override
        public void bindData(OrderViewItem item) {
            tvOrderNo.setText(mActivity.getString(R.string.order_no_label, item.order.getOrderNo()));
            tvStatus.setText(OrderUtils.getStatusLabel(mActivity, item.order.getStatus()));
            tvTakeUser.setText(mActivity.getString(R.string.take_user_label, item.order.getConsigneeName()));
            tvTakePhone.setText(mActivity.getString(R.string.take_phone_label, item.order.getConsigneePhone()));
        }
    }

    class GoodsHolder extends BaseViewHolder {
        View viewLine;
        ImageView ivGoods;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvGoodsPrice;

        public GoodsHolder(View itemView) {
            super(itemView);
            viewLine = itemView.findViewById(R.id.view_line);
            ivGoods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsNum = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
        }

        @Override
        public void bindData(OrderViewItem item) {
            viewLine.setVisibility(item.orderGoods.getIndex() == 0 ? View.GONE : View.VISIBLE);
            tvGoodsName.setText(item.orderGoods.getProductName());
            Glide.with(mActivity).load(item.orderGoods.getGoodsImg()).centerCrop().placeholder(R.mipmap.place_holder).into(ivGoods);
            char symbol = 165;
            tvGoodsPrice.setText(String.valueOf(symbol) + item.orderGoods.getAgentPrice());
            tvGoodsNum.setText("x" + item.orderGoods.getGoodsNum());
        }
    }

    public class BottomHolder extends BaseViewHolder {

        TextView tvGoodsNum;
        TextView tvTotalPrice;
        TextView tvExpressPrice;
        TextView tvCreateTime;
        TextView tvCancelOrder;
        TextView tvPayNow;
        TextView tvRefund;

        public BottomHolder(View itemView) {
            super(itemView);
            tvGoodsNum = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tvTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            tvExpressPrice = (TextView) itemView.findViewById(R.id.tv_express_price);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvCancelOrder = (TextView) itemView.findViewById(R.id.tv_cancel_order);
            tvPayNow = (TextView) itemView.findViewById(R.id.tv_pay);
            tvRefund = (TextView) itemView.findViewById(R.id.tv_refund);
        }

        @Override
        public void bindData(OrderViewItem item) {
            tvGoodsNum.setText(mActivity.getString(R.string.goods_num, item.order.getGoodsNum()));
            char symbol = 165;
//            double totalPrice = item.order.getGoodsprice() + item.order.getExpressPrice();
            String expressPrice = String.valueOf(symbol) + item.order.getExpressPrice();
            tvTotalPrice.setText(String.valueOf(symbol) + item.order.getTotalPrice());
            tvExpressPrice.setText(mActivity.getString(R.string.express_price, expressPrice));
            tvCreateTime.setText(StringUtils.getTimeLabelStr(DateUtils.longToStr(Long.valueOf(item.order.getCreateTime()),new SimpleDateFormat("yyyy-MM-dd"))));
            if (item.order.getStatus() == OrderUtils.REFUND_APPLY_FOR) {
                tvCancelOrder.setVisibility(View.GONE);
                tvPayNow.setVisibility(View.GONE);
                tvRefund.setVisibility(View.GONE);
//                tvCancelOrder.setOnClickListener(new OnOrderCancelClickListener(item.order));
//                tvPayNow.setOnClickListener(new OnPayClickListener(item.order));
            }else {
                tvRefund.setVisibility(View.GONE);
                tvCancelOrder.setVisibility(View.GONE);
                tvPayNow.setVisibility(View.GONE);
            }
        }
    }


    public void onDestroy() {
//        if (mCancelConfirmDialog != null) {
//            mCancelConfirmDialog.dismiss();
//        }
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(OrderViewItem item);
    }

    public static class OrderViewItem {
        public Order order;
        public OrderGoods orderGoods;
        public int type;

        public OrderViewItem(Order order, int type) {
            this.order = order;
            this.type = type;
        }

        public OrderViewItem(Order order, OrderGoods orderGoods, int type) {
            this.order = order;
            this.orderGoods = orderGoods;
            this.type = type;
        }
    }

    public static class OrderViewItemType {
        public static final int TOP = 0;
        public static final int GOODS = 1;
        public static final int BOTTOM = 2;
    }

}
