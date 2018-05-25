package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.view.NumberView;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.bumptech.glide.Glide;

import java.util.List;

public class GoodsSearchAdapter extends BaseRVAdapter<com.baigu.dms.domain.model.Goods> {
    public Activity mActivity;

    public GoodsSearchAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_search, parent, false);
        return new GoodsSearchAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        com.baigu.dms.domain.model.Goods goods = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Context context = itemViewHolder.itemView.getContext();
        Glide.with(context).load(goods.getCoverpath()).centerCrop().placeholder(R.mipmap.place_holder).into(itemViewHolder.ivGoods);
        itemViewHolder.tvGoodsName.setText(goods.getGoodsname());
        char symbol = 165;
        if (goods.getSkus().size() > 0){
            double minPrice = goods.getSkus().get(0).getUniformprice();
            if (goods.getSkus().size() > 1) {
                for (Sku sku : goods.getSkus()) {
                    if (sku.getUniformprice() < minPrice) {
                        minPrice = sku.getUniformprice();
                    }
                }
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + minPrice + "起");
            } else {
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + String.valueOf(minPrice));
            }
        }else {
            itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + String.valueOf(goods.getSkus().get(0).getUniformprice()));
        }
//        itemViewHolder.tvGoodsWeight.setText("/" + StringUtils.getWeightString(goods.getGoodsweight()));
        itemViewHolder.tvGoodsStock.setText(context.getString(R.string.stock_label, String.valueOf(goods.getStocknum())));
        itemViewHolder.tvGoodsStock.setVisibility(goods.getIsshow() == Goods.StockShowType.SHOW ? View.GONE : View.GONE);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGoods;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvGoodsWeight;
        TextView tvGoodsStock;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivGoods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsWeight = (TextView) itemView.findViewById(R.id.tv_goods_weight);
            tvGoodsStock = (TextView) itemView.findViewById(R.id.tv_goods_stock);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(GoodsSearchAdapter.this, getAdapterPosition());
                    }
                }
            });
        }
    }
}
