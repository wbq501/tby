package com.baigu.dms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.baigu.dms.common.view.OnRVItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/26 20:04
 */
public class BaseRVAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    protected List<T> mDataList = new ArrayList<>();

    protected OnRVItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnRVItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void clearData() {
        mDataList.clear();
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

    public void appendDataList(List<T> list) {
        if (list != null) {
            mDataList.addAll(list);
        }
    }

    public void appendData(T t) {
        mDataList.add(t);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }
}
