package com.baigu.dms.common.view.emotionskeyboard.interfaces;

import android.view.ViewGroup;

import com.baigu.dms.common.view.emotionskeyboard.adpater.EmoticonsAdapter;

public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
