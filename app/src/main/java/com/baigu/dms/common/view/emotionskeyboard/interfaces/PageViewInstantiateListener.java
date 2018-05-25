package com.baigu.dms.common.view.emotionskeyboard.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.baigu.dms.common.view.emotionskeyboard.data.PageEntity;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
