package com.baigu.dms.common.view.imagepicker;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Project iSphere
 * @Packate com.hy.imp.main.common.view
 *
 * @Description RecyclerView 分割线
 *
 * @Author Micky Liu
 * @Email liuhongwei@isphere.top
 * @Date 2016-08-01 16:16
 * @Company
 * @Copyright Copyright(C) 2016-2018
 * @Version 1.0.0
 */
public class ImagePickerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ImagePickerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
        outRect.top = parent.getChildLayoutPosition(view) < 4 ? space * 2 : space;
    }
}
