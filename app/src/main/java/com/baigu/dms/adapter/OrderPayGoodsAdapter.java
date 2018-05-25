package com.baigu.dms.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.OrderDetailGoods;
import com.bumptech.glide.Glide;

public class OrderPayGoodsAdapter extends BaseRVAdapter<OrderDetailGoods> {

    public Activity mActivity;

    public OrderPayGoodsAdapter(Activity activity) {
        mActivity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pay_goods, parent, false);
        return new OrderPayGoodsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        OrderDetailGoods orderGoods = mDataList.get(position);
        viewHolder.viewLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        viewHolder.tvGoodsName.setText(orderGoods.getGoodsname());
        char symbol = 165;
        viewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + orderGoods.getUniformprice());
        viewHolder.tvGoodsNum.setText("x" + orderGoods.getGoodsnum());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        View viewLine;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvGoodsPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            viewLine = itemView.findViewById(R.id.view_line);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsNum = (TextView) itemView.findViewById(R.id.tv_goods_num);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
        }

    }

}
