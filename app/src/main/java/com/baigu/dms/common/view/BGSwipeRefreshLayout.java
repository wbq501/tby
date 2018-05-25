package com.baigu.dms.common.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.baigu.dms.R;


public class BGSwipeRefreshLayout extends SwipeRefreshLayout {

    public BGSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public BGSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 设置进度圆圈上的颜色
        setColorSchemeResources(R.color.refresh_progress1, R.color.refresh_progress2, R.color.refresh_progress3, R.color.refresh_progress4);
        setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
    }
}
