package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DimView extends FrameLayout {

    public DimView(@NonNull Context context) {
        super(context);
    }

    public DimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DimView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
