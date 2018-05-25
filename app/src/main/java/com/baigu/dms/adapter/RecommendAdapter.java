package com.baigu.dms.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.ShopPictrue;
import com.baigu.dms.domain.model.Sku;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class RecommendAdapter extends BaseRVAdapter<Goods> {
    private Context context;
    private OnItemClickListener itemClickListener;


    public RecommendAdapter(Context context) {
        this.context = context;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Goods goods = getItem(position);
        RecommendViewHolder itemHolder = (RecommendViewHolder) holder;
        List<ShopPictrue> pics = goods.getPics();
        for (ShopPictrue shopPictrue : pics) {
            if (shopPictrue.getPosition() == 1) {
                ImageUtil.loadImage(context, shopPictrue.getPicUrl(), ((RecommendViewHolder) holder).icon);
            }
        }

        itemHolder.name.setText(goods.getGoodsname());
        if (TextUtils.isEmpty(goods.getGoodsdesc())) {
            itemHolder.message.setText("暂无介绍");
        } else {
            itemHolder.message.setText(goods.getGoodsdesc());
        }

        double price = 0;
        for (Sku sku : goods.getSkus()) {
            if (sku.getUniformprice() > price) {
                price = sku.getUniformprice();
            }
        }
        if (goods.getSkus().size() > 1) {
            itemHolder.price.setText("￥" + price + "起");
        } else {
            itemHolder.price.setText("￥" + price);
        }

    }

    class RecommendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView price;
        ImageView icon;
        TextView message;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_recommend_name);
            price = itemView.findViewById(R.id.tv_recommend_price);
            icon = itemView.findViewById(R.id.iv_recommend_icon);
            message = itemView.findViewById(R.id.tv_recommend_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
//            if (itemClickListener != null && position != 0 && position != 1) {
            Intent intent = new Intent(context, GoodsDetailActivity.class);
            intent.putExtra("goodsId", getItem(position).getIds());
            context.startActivity(intent);
//            }
        }
    }
}
