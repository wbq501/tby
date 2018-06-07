package com.baigu.dms.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.domain.model.Bank;
import com.hyphenate.helpdesk.model.Content;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class BankListAdater extends BaseRVAdapter<Bank> {

    private Context context;

    private OnItemClickListener onItemClickListener;
    private OnItemLongListener onItemLongListener;

    public BankListAdater(Context context){
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_list, parent, false);
        return new BankListAdater.BankHolder(view,onItemClickListener,onItemLongListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final Bank bank = getDataList().get(position);
        BankHolder itemHolder = (BankHolder) holder;
        itemHolder.name.setText(bank.getBankName());
        String number =bank.getBankAccount().substring(bank.getBankAccount().length()-4,bank.getBankAccount().length());
        itemHolder.bankNumber.setText(number);
        ImageUtil.loadImage(context,bank.getRemarks(),itemHolder.icon);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongListener(OnItemLongListener onItemLongListener) {
        this.onItemLongListener = onItemLongListener;
    }

    public class BankHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView name;
        TextView bankNumber;
        ImageView icon;
        OnItemClickListener itemClickListener;
        OnItemLongListener itemLongListener;

        public BankHolder(View itemView,OnItemClickListener itemClickListener,OnItemLongListener itemLongListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;
            this.itemLongListener = itemLongListener;
            name=itemView.findViewById(R.id.tv_bank_name);
            bankNumber=itemView.findViewById(R.id.tv_bank_number);
            icon=itemView.findViewById(R.id.iv_bank_icon);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(itemView,getLayoutPosition()-1);
            }
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongListener != null){
                itemLongListener.onItemClick(itemView,getLayoutPosition()-1);
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int postion);
    }

    public interface OnItemLongListener{
        void onItemClick(View view,int postion);
    }
}
