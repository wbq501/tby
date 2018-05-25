package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/8/22.
 */

public class NoChildEventRelativeLayout extends RelativeLayout {

    public NoChildEventRelativeLayout(Context context) {
        super(context);
    }

    public NoChildEventRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoChildEventRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
