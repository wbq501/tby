package com.baigu.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/26 20:04
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDataList = new ArrayList<>();
    protected LayoutInflater mLayoutInflater;

    public BaseListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void appendListData(List<T> list) {
        if (list != null) {
            mDataList.addAll(list);
        }
    }

    public void appendData(T t) {
        if (t != null) {
            mDataList.add(t);
        }
    }

    public void setData(List<T> list) {
        mDataList.clear();
        if (list != null) {
            mDataList.addAll(list);
        }
    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

}
