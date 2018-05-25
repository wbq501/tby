package com.baigu.dms.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.baigu.dms.R;

/**
 * @Description 自定义tab
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-03-16 下午 4:44
 */
public class TabButton extends RelativeLayout {
    private RadioButton mRadioButton;
    private ImageView mBadgeImageView;
    private int mPosition;

    public TabButton(Context context) {
        super(context);
    }

    public TabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabButton, defStyle, 0);
        mPosition = a.getInt(R.styleable.TabButton_tb_position, 0);
        String text = a.getString(R.styleable.TabButton_tb_text);
        int textColor = a.getColor(R.styleable.TabButton_tb_textColor, 0XFFFFFFFF);
        Drawable buttonImgDrawable = a.getDrawable(R.styleable.TabButton_tb_buttonImg);
        Drawable badgeImgDrawable = a.getDrawable(R.styleable.TabButton_tb_badgeImg);

        mRadioButton = new RadioButton(getContext());
        mRadioButton.setId(Integer.valueOf(1));
        mRadioButton.setClickable(false); //屏蔽点击事件
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        mRadioButton.setButtonDrawable(transparentDrawable);
        mRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, buttonImgDrawable, null, null);
        mRadioButton.setCompoundDrawablePadding(1);
        mRadioButton.setPadding(5, 0, 5, 0);
        mRadioButton.setBackgroundDrawable(transparentDrawable);
        mRadioButton.setTextSize(11);
        mRadioButton.setText(text);
        mRadioButton.setTextColor(textColor);
        mRadioButton.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        this.addView(mRadioButton, params);

        mBadgeImageView = new ImageView(getContext());
        mBadgeImageView.setImageDrawable(badgeImgDrawable);
        mBadgeImageView.setVisibility(View.GONE); //默认隐藏
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_TOP, mRadioButton.getId()); //必须首先设置mRadioButton的ID
        params.addRule(RelativeLayout.RIGHT_OF, mRadioButton.getId());
        params.leftMargin = -2;
        addView(mBadgeImageView, params);
        a.recycle();
    }

    public void setText(int resId) {
        mRadioButton.setText(resId);
    }

    public void setChecked(boolean isChekced) {
        mRadioButton.setChecked(isChekced);
        mRadioButton.setTextColor(getResources().getColor(isChekced ? R.color.colorPrimary : R.color.main_text));
    }

    public void setShowBadge(boolean isShow) {
        int visibility = isShow ? View.VISIBLE : View.INVISIBLE;
        mBadgeImageView.setVisibility(visibility);
    }

    public int getPosition() {
        return mPosition;
    }

}
