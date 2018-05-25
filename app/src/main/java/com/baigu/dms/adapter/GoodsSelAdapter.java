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
import com.baigu.dms.common.utils.DecimalUtils;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;

/**
 * @Description 已选择的商品列表
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/26 23:11
 */
public class GoodsSelAdapter extends BaseListAdapter<Goods> {

    public GoodsSelAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_goods_sel, null);
            holder = new Holder();
            holder.ivGoods = (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_realname);
            holder.tvAgentPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvSelNum = (TextView) convertView.findViewById(R.id.tv_sel_num);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
            holder.tvSku = (TextView) convertView.findViewById(R.id.tv_sku);
            holder.tvPrice_all = (TextView) convertView.findViewById(R.id.tv_price_all);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Goods goods = getItem(position);
        Glide.with(mContext).load(goods.getCoverpath()).centerCrop().placeholder(R.mipmap.place_holder).into(holder.ivGoods);
        holder.tvName.setText(goods.getGoodsname());
        holder.tvSelNum.setText(mContext.getString(R.string.buy_num, String.valueOf(goods.getBuyNum())));
        holder.tvAgentPrice.setText("￥" + String.valueOf(goods.getUniformprice()));
        holder.tvTotalPrice.setText(mContext.getString(R.string.total_price2, String.format("%.2f",goods.getBuyNum() * goods.getUniformprice())));
        String markPrice="原价:"+ DecimalUtils.wodecimal(goods.getMarketprice()*goods.getBuyNum());
        SpannableString spanString = new SpannableString(markPrice);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, 0, markPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPrice_all.setText(spanString);
        Sku sku = goods.getSkus().get(0);
        try {
            StringBuilder tv_sku = new StringBuilder();
            JSONArray array = new JSONArray(sku.getSkuAttr());
            for (int j = 0; j < array.length(); j++) {
                if (j > 0) {
                    tv_sku.append("+");
                }
                tv_sku.append(array.getJSONObject(j).getString("name"));
            }
            sku.setSkuAttr(tv_sku.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tvSku.setText(goods.getSkus().get(0).getSkuAttr());
        return convertView;
    }

    class Holder {
        ImageView ivGoods;
        TextView tvName;
        TextView tvAgentPrice;
        TextView tvSelNum;
        TextView tvSku;
        TextView tvTotalPrice;
        TextView tvPrice_all;
    }
}
