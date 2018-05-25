package com.baigu.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.baigu.dms.R;
import com.baigu.dms.common.view.emotionskeyboard.hhboard.HHFuncView;

import java.util.ArrayList;

/**
 * @Project iSphere
 * @Packate com.hy.imp.main.adapter
 * 
 * @Description
 * 
 * @Author Micky Liu
 * @Email liuhongwei@isphere.top
 * @Date 2016-05-17 18:22
 * @Company 北京华云合创科技有限公司成都分公司
 * @Copyright Copyright(C) 2016-2018
 * @Version 1.0.0
 */
public class ChatBottomFuncAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<HHFuncView.FuncItem> mDdata = new ArrayList<HHFuncView.FuncItem>();

    private HHFuncView.FuncViewClickListener mFuncViewClickListener;

    public void setFuncViewClickListener(HHFuncView.FuncViewClickListener funcViewClickListener) {
        mFuncViewClickListener = funcViewClickListener;
    }

    public ChatBottomFuncAdapter(Context context, ArrayList<HHFuncView.FuncItem> data) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        if (data != null) {
            this.mDdata = data;
        }
    }

    @Override
    public int getCount() {
        return mDdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mDdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_chat_bottom_func, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_realname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final HHFuncView.FuncItem FuncItem = mDdata.get(position);
        if (FuncItem != null) {
            viewHolder.iv_icon.setBackgroundResource(FuncItem.getIcon());
            viewHolder.tv_name.setText(FuncItem.getFuncName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, FuncItem.getFuncName(), Toast.LENGTH_SHORT).show();
                    if (mFuncViewClickListener != null) {
                        mFuncViewClickListener.onFuncViewClick(FuncItem.getId());
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_name;
    }
}