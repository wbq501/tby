package com.baigu.dms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.BankType;

public class BankTypeAdapter extends BaseRVAdapter<BankType> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_type_selector, parent, false);
        return new BankTypeAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BankType bankType = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tvName.setText(bankType.getName());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_bank_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(BankTypeAdapter.this, getAdapterPosition());
                    }
                }
            });
        }
    }
}
