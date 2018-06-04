package com.baigu.dms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.domain.model.RecommendClass;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class RecommendClassAdapter extends BaseRVAdapter<RecommendClass> {

    private Context context;
    private OnItemClickListener itemClickListener;

    public RecommendClassAdapter(Context context) {
        this.context = context;

    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        RecommendClass recommendClass = getItem(position);
        RecommendClassViewHolder mholder = (RecommendClassViewHolder) holder;
        mholder.mclass.setText(recommendClass.getName());
        ImageUtil.loadImage(context, recommendClass.getImg(), mholder.icon);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_class, parent, false);
        return new RecommendClassViewHolder(view);
    }


    class RecommendClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView mclass;


        public RecommendClassViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_recommend);
            mclass = itemView.findViewById(R.id.tv_description);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int widthPixels = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) icon.getLayoutParams();
            params.weight = widthPixels/4;
            params.height = widthPixels/4-20;
            icon.setLayoutParams(params);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.OnItemClick(view, getLayoutPosition());
            }
        }
    }
}
