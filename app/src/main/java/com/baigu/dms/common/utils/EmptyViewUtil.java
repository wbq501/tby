package com.baigu.dms.common.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;

/**
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/7 10:35
 */
public class EmptyViewUtil {
    public static void initData(Activity activity, int tipResId) {
        TextView tvEmptyTip = (TextView) activity.findViewById(R.id.tv_empty_tip);
        tvEmptyTip.setText(tipResId);
    }

    public static void show(Activity activity) {
        activity.findViewById(R.id.ll_empty).setVisibility(View.VISIBLE);
    }

    public static void hide(Activity activity) {
        activity.findViewById(R.id.ll_empty).setVisibility(View.GONE);
    }

    public static void initData(View view, int tipResId) {
        TextView tvEmptyTip = (TextView) view.findViewById(R.id.tv_empty_tip);
        tvEmptyTip.setText(tipResId);
    }

    public static void show(View view) {
        view.findViewById(R.id.ll_empty).setVisibility(View.VISIBLE);
    }

    public static void hide(View view) {
        view.findViewById(R.id.ll_empty).setVisibility(View.GONE);
    }
}
