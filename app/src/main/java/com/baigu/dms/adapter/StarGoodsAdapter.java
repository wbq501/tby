package com.baigu.dms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Goods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:28
 */
public class StarGoodsAdapter extends BaseRVAdapter<Goods> {

    public static final int TYPE_DEFAULT = 1;
    public static final int TYPE_2 = 2;

    private int mRvWidth;

    public StarGoodsAdapter(Context context) {
        mRvWidth = ViewUtils.getScreenInfo(context).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_star_goods, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_DEFAULT;
        if (position == 0 || position == 1) {
            type = TYPE_2;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Goods goods = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//        itemViewHolder.tvName.setText(goods.getGoodsname());
//        itemViewHolder.tvDesc.setText(goods.getGoodsdesc());
//        switch (getItemViewType(position)) {
//            case TYPE_2:
//                itemViewHolder.iv.getLayoutParams().width = mRvWidth / 2;
//                itemViewHolder.iv.getLayoutParams().height = itemViewHolder.iv.getLayoutParams().width;
//                break;
//            default:
//                itemViewHolder.iv.getLayoutParams().width = mRvWidth / 3;
//                itemViewHolder.iv.getLayoutParams().height = (int) (itemViewHolder.iv.getLayoutParams().width * 1.5);
//                break;
//        }
//        itemViewHolder.itemView.getLayoutParams().width = itemViewHolder.iv.getLayoutParams().width;
//        itemViewHolder.itemView.getLayoutParams().height = itemViewHolder.iv.getLayoutParams().height;
        itemViewHolder.title.setText(goods.getGoodsname());
        itemViewHolder.tvDesc.setText(goods.getGoodsdesc());
        Glide.with(itemViewHolder.itemView.getContext()).load(goods.getSupercoverpath()).placeholder(R.mipmap.place_holder).into(itemViewHolder.iv);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView title;
        TextView tvDesc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.iv = (ImageView) itemView.findViewById(R.id.iv);
            this.title = (TextView) itemView.findViewById(R.id.tv_title);
//            this.tvName.setVisibility(View.GONE);
            this.tvDesc = (TextView) itemView.findViewById(R.id.tv_description);
            this.tvDesc.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.tvDesc.setSingleLine(true);
            this.tvDesc.setSelected(true);
            this.tvDesc.setFocusable(true);
            this.tvDesc.setFocusableInTouchMode(true);
//            this.tvDesc.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(StarGoodsAdapter.this, getAdapterPosition());
                    }
                }
            });
        }
    }


}