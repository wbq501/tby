package com.baigu.dms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.ExpressInfo;

public class ExpressStateAdapter extends BaseRVAdapter<ExpressInfo>{

    private Context context;

    public ExpressStateAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exprss_state_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpressInfo expressInfo = (ExpressInfo) mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tv_year.setText(expressInfo.getTime().substring(0,10));
        itemViewHolder.tv_time.setText(expressInfo.getTime().substring(expressInfo.getTime().length()-8,expressInfo.getTime().length()));
        itemViewHolder.tv_express_speed.setText(expressInfo.getContext());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_year,tv_time,tv_express_speed;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_express_speed = itemView.findViewById(R.id.tv_express_speed);
        }
    }
}
