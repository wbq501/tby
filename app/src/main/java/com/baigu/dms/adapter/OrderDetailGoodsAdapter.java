package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.AddressAddEditActivity;
import com.baigu.dms.activity.AddressListActivity;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.OrderDetailGoods;
import com.baigu.dms.domain.model.OrderGoods;
import com.baigu.dms.presenter.AddressPresenter;
import com.bumptech.glide.Glide;

public class OrderDetailGoodsAdapter extends BaseRVAdapter<OrderGoods> {

    public Activity mActivity;

    public OrderDetailGoodsAdapter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_goods, parent, false);
        return new OrderDetailGoodsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        final OrderGoods orderGoods = mDataList.get(position);
        viewHolder.viewLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        viewHolder.tvGoodsName.setText(orderGoods.getProductName());
        Glide.with(mActivity).load(orderGoods.getGoodsImg()).centerCrop().placeholder(R.mipmap.place_holder).into(viewHolder.ivGoods);
        char symbol = 165;
        viewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + orderGoods.getAgentPrice());
        viewHolder.tvGoodsNum.setText("x" + orderGoods.getGoodsNum());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                intent.putExtra("goodsId", orderGoods.getProductId());
                mActivity.startActivity(intent);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        View viewLine;
        ImageView ivGoods;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvGoodsPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            viewLine = itemView.findViewById(R.id.view_line);
            ivGoods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsNum = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
        }

    }

}
