package com.baigu.dms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class GoodsCategoryAdater extends BaseRVAdapter<GoodsCategory> {

    private Context context;
    private int selet;
    private OnItemClickListener itmListener;

    public void setItmListener(OnItemClickListener itmListener) {
        this.itmListener = itmListener;
    }

    public GoodsCategoryAdater(Context context) {
        this.context = context;
    }

    public void setSelet(int selet) {
        this.selet = selet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_category, parent, false);
        return new GoodsCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        GoodsCategory category = getItem(position);
        holder = (GoodsCategoryViewHolder) holder;
        if (selet == position) {
            ((GoodsCategoryViewHolder) holder).selectView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            ((GoodsCategoryViewHolder) holder).selectView.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
        }

        ((GoodsCategoryViewHolder) holder).category.setText(category.getName());

        if (category.getNumber() > 0) {
            ((GoodsCategoryViewHolder) holder).number.setText(category.getNumber() + "");
            ((GoodsCategoryViewHolder) holder).number.setVisibility(View.VISIBLE);
        } else {
            ((GoodsCategoryViewHolder) holder).number.setVisibility(View.GONE);
        }
    }

    class GoodsCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView category;
        TextView number;
        RelativeLayout selectView;

        public GoodsCategoryViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.tv_goods_category);
            number = itemView.findViewById(R.id.tv_number);
            selectView = itemView.findViewById(R.id.rl_goods_category);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            itmListener.onItemClick(view, getLayoutPosition());
        }
    }


}
