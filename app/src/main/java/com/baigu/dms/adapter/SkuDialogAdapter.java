package com.baigu.dms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.common.view.NumberView;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;


import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class SkuDialogAdapter extends BaseRVAdapter<Sku> {

    private Context context;
    private int selsed=0;
    private Map<String ,Integer> mapNumber;
    private NumberView numberView;
    private OnItemClickListener listener;

    public SkuDialogAdapter(Context context) {
        this.context = context;
    }

    public int getSelsed() {
        return selsed;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_goods_specification_dialog,null);
        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Sku sku = getItem(position);
        MyHolder itemHolder = (MyHolder) holder;
        itemHolder.skunName.setText(sku.getSkuAttr());
        if(selsed==position){
            ((MyHolder) holder).skunName.setTextColor(context.getResources().getColor(R.color.white));
            ((MyHolder) holder).skunName.setBackground(context.getResources().getDrawable(R.drawable.bg_btn_goods_specification_selecte));
        }else{
            ((MyHolder) holder).skunName.setTextColor(context.getResources().getColor(R.color.color_111111));
            ((MyHolder) holder).skunName.setBackground(context.getResources().getDrawable(R.drawable.bg_btn_goods_specification_unselecte));
        }

//        if (sku.getNumber() > 0){
//            ((MyHolder) holder).number.setText(sku.getNumber()+"");
//            ((MyHolder) holder).number.setVisibility(View.VISIBLE);
//        }else {
//            ((MyHolder) holder).number.setVisibility(View.GONE);
//        }

        if( mapNumber != null && mapNumber.containsKey(getDataList().get(position).getSkuId()) ){
            if(mapNumber.get(getDataList().get(position).getSkuId())>0){
                ((MyHolder) holder).number.setText(mapNumber.get(getDataList().get(position).getSkuId())+"");
                ((MyHolder) holder).number.setVisibility(View.VISIBLE);
            }else{
                ((MyHolder) holder).number.setVisibility(View.GONE);
            }
        }

    }



    public void setSelsed(int selsed) {
        this.selsed = selsed;
        notifyDataSetChanged();
    }

    public void setMapNumber(Map<String, Integer> mapNumber) {
        this.mapNumber = mapNumber;
    }

    public void setNumber(NumberView number) {
        this.numberView = number;
    }

    public void upDataNumber(int amount) {
        if(mapNumber != null && mapNumber.containsKey(getDataList().get(getSelsed()).getSkuId())){
            mapNumber.put(getDataList().get(getSelsed()).getSkuId(),amount);
            notifyDataSetChanged();
        }

    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button skunName;
        TextView number;

        public MyHolder(View itemView) {
            super(itemView);
            skunName = itemView.findViewById(R.id.btn_goods_specification);
            number = itemView.findViewById(R.id.tv_goods_specification_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            setSelsed(getLayoutPosition());
            if(numberView!= null){
//                List<Sku> dataList = getDataList();
//                numberView.setCurrNum(dataList.get(getLayoutPosition()).getNumber());
                numberView.setCurrNum(mapNumber.get(getDataList().get(getLayoutPosition()).getSkuId()));
            }
            if(listener != null){
                listener.OnItemClick(view,getLayoutPosition());
            }


        }
    }

}
