package com.baigu.dms.common.view.shinebutton;

import android.animation.ValueAnimator;
import android.graphics.Canvas;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/3 23:28
 */
public class ShineAnimator extends ValueAnimator {

    float MAX_VALUE = 1.5f;
    long ANIM_DURATION = 1500;
    Canvas canvas;

    ShineAnimator() {
        setFloatValues(1f, MAX_VALUE);
        setDuration(ANIM_DURATION);
        setStartDelay(200);
        setInterpolator(new EasingInterpolator(Ease.QUART_OUT));
    }
    ShineAnimator(long duration,float max_value,long delay) {
        setFloatValues(1f, max_value);
        setDuration(duration);
        setStartDelay(delay);
        setInterpolator(new EasingInterpolator(Ease.QUART_OUT));
    }

    public void startAnim(final ShineView shineView, final int centerAnimX, final int centerAnimY) {

        start();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


}