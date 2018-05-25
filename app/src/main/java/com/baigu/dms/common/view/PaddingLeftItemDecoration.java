package com.baigu.dms.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

public class PaddingLeftItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mPaddingLeft = 0;
    private boolean mShowFirst = true;

    public PaddingLeftItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.divider);
    }

    public PaddingLeftItemDecoration(Context context, int paddingLeft) {
        mDivider = context.getResources().getDrawable(R.drawable.divider);
        mPaddingLeft = paddingLeft;
    }

    public PaddingLeftItemDecoration(Context context, int paddingLeft, boolean showFirst) {
        mDivider = context.getResources().getDrawable(R.drawable.divider);
        mPaddingLeft = paddingLeft;
        mShowFirst = showFirst;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = ViewUtils.dip2px(mPaddingLeft);
        int right = parent.getWidth(); //parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (!mShowFirst) {
                int position = parent.getChildLayoutPosition(child);
                if (position == 0) {
                    continue;
                }
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
