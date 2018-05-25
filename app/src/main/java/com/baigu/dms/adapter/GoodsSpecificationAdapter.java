package com.baigu.dms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.domain.model.Sku;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class GoodsSpecificationAdapter extends BaseRVAdapter<Sku> {

    private Context context;
    private int position;
    private OnItemClickListener listener;

    public GoodsSpecificationAdapter(Context context) {
        this.context = context;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_specification,parent,false);
        return new SpecificationHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Sku sku = getItem(position);
        SpecificationHolder itemHolder = (SpecificationHolder) holder;

        StringBuilder tv_sku=new StringBuilder();
        try {
            JSONArray array=new JSONArray(sku.getSkuAttr());
            for (int j = 0; j < array.length(); j++) {
                if(j>0){
                    tv_sku.append("+");
                }
                tv_sku.append(array.getJSONObject(j).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemHolder.item.setText(tv_sku);
        if(this.position == position){
            itemHolder.item.setTextColor(context.getResources().getColor(R.color.white));
            itemHolder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_btn_goods_specification_selecte));
       }else{
            itemHolder.item.setTextColor(context.getResources().getColor(R.color.color_111111));
            itemHolder.item.setBackground(context.getResources().getDrawable(R.drawable.bg_btn_goods_specification_unselecte));
        }

    }

    class  SpecificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item;

        public SpecificationHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.tv_goods_specification);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener !=null){
                setPosition(getLayoutPosition());
                listener.OnItemClick(view,getLayoutPosition());
                notifyDataSetChanged();
            }
        }
    }



}
