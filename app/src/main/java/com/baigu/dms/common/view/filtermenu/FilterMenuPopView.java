package com.baigu.dms.common.view.filtermenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/7/3 23:12
 */
public class FilterMenuPopView {

    private PopupWindow mPopupWindow;
    private ExpandFilterMenuView.MenuItemData mMenuItemData;

    public FilterMenuPopView(Context context) {
        if (mPopupWindow == null) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.view_filter_menu_content, null);
            contentView.findViewById(R.id.view_place_holder).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mPopupWindow.dismiss();
                    return true;
                }
            });
            mPopupWindow = new PopupWindow(contentView);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            ColorDrawable colorDrawable = new ColorDrawable(0xA0000000);
            mPopupWindow.setBackgroundDrawable(colorDrawable);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(false);
        }
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        mPopupWindow.setOnDismissListener(onDismissListener);
    }

    public void setData(ExpandFilterMenuView.MenuItemData menuItemData) {
        mMenuItemData = menuItemData;
    }

    public ExpandFilterMenuView.MenuItemData getMenuItemData() {
        return mMenuItemData;
    }

    public void show(View locView) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            int[] location = new int[2];
            locView.getLocationOnScreen(location);
            mPopupWindow.setHeight(ViewUtils.getScreenInfo(locView.getContext()).heightPixels - location[1] - ViewUtils.dip2px(0.5F));
            mPopupWindow.showAsDropDown(locView, 0, locView.getHeight());
        }
    }

    public void hide() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}
