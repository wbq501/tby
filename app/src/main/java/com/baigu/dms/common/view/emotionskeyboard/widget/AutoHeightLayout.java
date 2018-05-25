package com.baigu.dms.common.view.emotionskeyboard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.view.emotionskeyboard.utils.EmoticonsKeyboardUtils;

public abstract class AutoHeightLayout extends SoftKeyboardSizeWatchLayout implements SoftKeyboardSizeWatchLayout.OnResizeListener {

    private static final int ID_CHILD = R.id.id_autolayout;

    protected int mSoftKeyboardHeight;
    protected int mMaxParentHeight;
    protected Context mContext;

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mSoftKeyboardHeight = EmoticonsKeyboardUtils.getDefKeyboardHeight(mContext);
        addOnResizeListener(this);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = getChildCount();
        if (childSum > 1) {
            throw new IllegalStateException("can host only one direct child");
        }
        super.addView(child, index, params);
        if (childSum == 0) {
            if (child.getId() < 0) {
                child.setId(ID_CHILD);
            }
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ALIGN_PARENT_BOTTOM);
            child.setLayoutParams(paramsChild);
        } else if (childSum == 1) {
            RelativeLayout.LayoutParams paramsChild = (RelativeLayout.LayoutParams) child.getLayoutParams();
            paramsChild.addRule(ABOVE, ID_CHILD);
            child.setLayoutParams(paramsChild);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    public void updateMaxParentHeight(int maxParentHeight) {
        this.mMaxParentHeight = maxParentHeight;
        if(maxParentHeightChangeListener != null){
            maxParentHeightChangeListener.onMaxParentHeightChange(maxParentHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxParentHeight != 0) {
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void OnSoftPop(final int height) {
        if (mSoftKeyboardHeight != height) {
            mSoftKeyboardHeight = height;
            EmoticonsKeyboardUtils.setDefKeyboardHeight(mContext, mSoftKeyboardHeight);
            onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
        }
    }

    @Override
    public void OnSoftClose() { }

    public abstract void onSoftKeyboardHeightChanged(int height);

    private OnMaxParentHeightChangeListener maxParentHeightChangeListener;

    public interface OnMaxParentHeightChangeListener {
        void onMaxParentHeightChange(int height);
    }

    public void setOnMaxParentHeightChangeListener(OnMaxParentHeightChangeListener listener) {
        this.maxParentHeightChangeListener = listener;
    }
}