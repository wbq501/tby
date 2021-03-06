package com.baigu.dms.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baigu.dms.R;

public class HorizontalItemNoneSEDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public HorizontalItemNoneSEDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.list_horizontal_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1) {
                continue;
            }
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();


            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}

