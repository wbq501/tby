package com.baigu.dms.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.view.NumberView;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 购物车window
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/26 23:11
 */
public class ShopWindowAdapter extends BaseListAdapter<Goods> {

    private TotalChangeListener changeListener;

    public void setChangeListener(TotalChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public ShopWindowAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_shopwindow, null);
            holder = new Holder();
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_realname);
            holder.tvAgentPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.numberView = (NumberView) convertView.findViewById(R.id.number_view);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
            holder.tvSku = (TextView) convertView.findViewById(R.id.tv_sku);
            holder.tvPriceAll = (TextView) convertView.findViewById(R.id.tv_price_all);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final Goods goods = getItem(position);
        Glide.with(mContext).load(goods.getCoverpath()).centerCrop().placeholder(R.mipmap.place_holder).into(holder.ivGoods);
        holder.tvName.setText(goods.getGoodsname());
//        holder.tvSelNum.setText(mContext.getString(R.string.buy_num, String.valueOf(goods.getBuyNum())));
        holder.tvAgentPrice.setText("￥" + String.valueOf(goods.getUniformprice()));
        holder.tvTotalPrice.setText(mContext.getString(R.string.total_price2, String.format("%.2f", goods.getBuyNum() * goods.getUniformprice())));

        String markPrice = "原价:" + String.format("%.2f",goods.getMarketprice() * goods.getBuyNum());
        SpannableString spanString = new SpannableString(markPrice);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, 0, markPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPriceAll.setText(spanString);
        Sku sku = goods.getSkus().get(0);
        try {
            StringBuilder tv_sku = new StringBuilder();
            JSONArray array = new JSONArray(sku.getSkuAttr());
            for (int j = 0; j < array.length(); j++) {
                if (j > 0) {
                    tv_sku.append("+");
                }
                tv_sku.append(array.getJSONObject(j).getString("value"));
            }
            sku.setSkuAttr(tv_sku.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tvSku.setText(goods.getSkus().get(0).getSkuAttr());
        holder.numberView.setMaxNum(goods.getStocknum());
        holder.numberView.setCurrNum(goods.getSkus().get(0).getNumber());
        holder.numberView.setSku(goods.getSkus().get(0));
        holder.numberView.setOnNumChangeListener(new NumberView.OnNumChangeListener() {
            @Override
            public boolean onAbleChanged(int currNum) {
                return true;
            }

            @Override
            public void onNumChanged(int amount) {
                getItem(position).getSkus().get(0).setNumber(amount);
                ArrayList<Goods> listSelected = ShopCart.getGoodsListSelected();
                for (Goods mGoods : listSelected) {
                    if (mGoods.getIds().equals(getItem(position).getIds())) {
                        for (Sku sku : mGoods.getSkus()) {
                                if (getItem(position).getSkus().get(0).getSkuId().equals(sku.getSkuId())) {
                                    sku.setNumber(amount);
                                }
                        }
                    }
                }
//                ShopCart.addGoods(getItem(position));
                ShopCart.checkGoods();
                holder.tvTotalPrice.setText(mContext.getString(R.string.total_price2, String.format("%.2f", amount * goods.getUniformprice())));
                String markPrice = "原价:" + String.format("%.2f",goods.getMarketprice() * amount);
                SpannableString spanString = new SpannableString(markPrice);
                StrikethroughSpan span = new StrikethroughSpan();
                spanString.setSpan(span, 0, markPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvPriceAll.setText(spanString);
                if (changeListener != null) {
                    changeListener.TotalChange();
                }
            }
        });
        return convertView;
    }

    class Holder {
        ImageView ivGoods;
        TextView tvName;
        TextView tvAgentPrice;
        NumberView numberView;
        TextView tvSku;
        TextView tvTotalPrice;
        TextView tvPriceAll;
    }

    public interface TotalChangeListener {
        void TotalChange();
    }
}
