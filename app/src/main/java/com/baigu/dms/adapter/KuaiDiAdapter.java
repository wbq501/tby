package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.ExpressWebActivity;
import com.baigu.dms.common.view.ExpressDialog;
import com.baigu.dms.domain.model.Express;
import com.baigu.dms.domain.model.ExpressList;

import java.util.List;

public class KuaiDiAdapter extends BaseRVAdapter<ExpressList>{

    public Activity mActivity;
    private List<ExpressList> lists;

    public KuaiDiAdapter(Activity activity,List<ExpressList> lists) {
        this.mActivity = activity;
        this.lists = lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.exprss_list_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ExpressList expressList = lists.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.mTextView.setText(expressList.getName());
        itemViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressDialog dialog = new ExpressDialog(mActivity,mActivity,expressList);
                dialog.show();
            }
        });
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_name);
        }
    }
}
