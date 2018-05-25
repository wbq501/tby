package com.baigu.dms.common.view.textstyleplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;


import com.baigu.dms.common.view.VerticalImageSpan;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * StyleItem <br/>
 * Created by xiaqiulei on 2015-07-27.
 */
public class TextStyleItem implements ISpannable {

    private static final float DEFAULT_ALPHA = 0.20F;


    @IntDef({Typeface.NORMAL, Typeface.BOLD, Typeface.BOLD_ITALIC, Typeface.ITALIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeFaceStyle {
    }

    public interface OnClickListener {
        void onClick(String clickedText);
    }

    public interface OnLongClickListener {
        void onLongClick(String clickedText);
    }

    protected final String text;
    protected int textSize = 0;// 字体大小
    protected int textColor = 0;// 字体颜色
    protected float highlightAlpha = DEFAULT_ALPHA; // 按下的颜色
    protected int backgroundColor;// 背景颜色
    protected int backgroundColorRes;// 背景颜色
    protected int typeFaceStyle = Typeface.NORMAL; // 样式

    protected int iconRes;
    protected Drawable iconDrawable;
    protected Bitmap iconBitmap;

    protected boolean underLined = false; // 下划线
    protected boolean strikethrough = false; // 中划线
    protected boolean superscript = false; // 上坐标
    protected boolean subscript = false;// 下坐标

    protected OnClickListener clickListener; // 点击事件
    protected OnLongClickListener longClickListener;// 长按事件

    public TextStyleItem(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public OnClickListener getClickListener() {
        return clickListener;
    }

    public OnLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public String getText() {
        return text;
    }

    public TextStyleItem setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;

        return this;
    }

    public TextStyleItem setBackgroundColorRes(@ColorRes int backgroundColorRes) {
        this.backgroundColorRes = backgroundColorRes;
        return this;
    }

    public TextStyleItem setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public TextStyleItem setHighlightAlpha(float highlightAlpha) {
        this.highlightAlpha = highlightAlpha;
        return this;
    }

    public TextStyleItem setLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public TextStyleItem setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public TextStyleItem setSubscript(boolean subscript) {
        this.subscript = subscript;
        return this;
    }

    public TextStyleItem setSuperscript(boolean superscript) {
        this.superscript = superscript;
        return this;
    }

    public TextStyleItem setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    public TextStyleItem setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public TextStyleItem setTypeFaceStyle(@TypeFaceStyle int typeFaceStyle) {
        this.typeFaceStyle = typeFaceStyle;
        return this;
    }

    public TextStyleItem setUnderLined(boolean underLined) {
        this.underLined = underLined;
        return this;
    }

    public TextStyleItem setIconRes(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public TextStyleItem setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    public TextStyleItem setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
        return this;
    }

    @Override
    public SpannableString makeSpannableString(Context context) {
        SpannableString spannableString = new SpannableString(text);
        int length = spannableString.length();

        // strikethrough
        if (strikethrough) {
            spannableString.setSpan(new StrikethroughSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // underLined
        if (underLined) {
            spannableString.setSpan(new UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // subscript
        if (subscript) {
            spannableString.setSpan(new SubscriptSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // superscript
        if (superscript) {
            spannableString.setSpan(new SuperscriptSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // click listener or long click listener
        if (clickListener != null || longClickListener != null) {
            TouchableSpan span = new TouchableSpan(context, this);
            span.setUnderLined(underLined);
            spannableString.setSpan(span, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // style
        spannableString.setSpan(new StyleSpan(typeFaceStyle), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // background color
        if (backgroundColor == 0 && backgroundColorRes != 0) {
            backgroundColor = context.getResources().getColor(backgroundColorRes);
        }
        if (backgroundColor != 0) {
            spannableString.setSpan(new BackgroundColorSpan(backgroundColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // text color
        if (textColor != 0) {
            spannableString.setSpan(new ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // textSize
        if (textSize != 0) {
            spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // image
        if (iconBitmap == null && iconRes != 0) {
            iconBitmap = BitmapFactory.decodeResource(context.getResources(), iconRes);
        }
        if (iconBitmap == null && iconDrawable != null) {
            spannableString.setSpan(new VerticalImageSpan(iconDrawable), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (iconBitmap != null) {
            spannableString.setSpan(new VerticalImageSpan(iconBitmap), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }
}