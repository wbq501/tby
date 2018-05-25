package com.baigu.dms.common.view.filtermenu;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 分类筛选Tab导航（可弹出筛选选项）
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 0:13
 */
public class ExpandFilterMenuView extends RelativeLayout implements FilterMenuView.OnMenuSelectedListener, PopupWindow.OnDismissListener {
    private int mTabPadding = 0;
    private List<MenuItemData> mMenuItemDataList = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private FilterMenuPopView mFilterMenuPopView;
    private View mLine;
    private List<FilterMenuView> mFilterMenuViewList = new ArrayList<>();

    public ExpandFilterMenuView(Context context) {
        super(context);
        initView();
    }

    public ExpandFilterMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExpandFilterMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mFilterMenuPopView = new FilterMenuPopView(getContext());
        mFilterMenuPopView.setOnDismissListener(this);

        addLine();
        mTabPadding = ViewUtils.dip2px(5);
        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mLinearLayout, params);
    }

    public void setMenuItemDataList(List<MenuItemData> list) {
        mMenuItemDataList.clear();
        mMenuItemDataList.addAll(list);
        updateView();
    }

    private void updateView() {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mMenuItemDataList.size(); i++) {
            addTab(mMenuItemDataList.get(i));
        }
    }

    private void addTab(MenuItemData menuItemData) {
        int width = (ViewUtils.getScreenInfo(getContext()).widthPixels - (10 * mTabPadding)) / 4;
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuParams.topMargin = mTabPadding;
        FilterMenuView menuView = new FilterMenuView(getContext(), mTabPadding);
        menuView.setMenuItemData(menuItemData);
        menuView.setOnMenuSelectedListener(this);
        menuParams.leftMargin = mTabPadding * 2;
        mLinearLayout.addView(menuView, menuParams);
        mFilterMenuViewList.add(menuView);
    }

    private void addLine() {
        mLine = new View(getContext());
        mLine.setBackgroundColor(getResources().getColor(R.color.line));
        LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtils.dip2px(0.4f));
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mLine, lineParams);
    }

    @Override
    public boolean onOtherShowed(MenuItemData tabData) {
        if (mFilterMenuPopView != null && mFilterMenuPopView.getMenuItemData() != null && mFilterMenuPopView.getMenuItemData().id != tabData.id) {
            mFilterMenuPopView.hide();
            return true;
        }
        return false;
    }

    @Override
    public void onSelected(MenuItemData tabData) {
        if (mFilterMenuPopView != null) {
            mFilterMenuPopView.setData(tabData);
            mFilterMenuPopView.show(mLine);
        }
    }

    @Override
    public void onUnSelected(MenuItemData tabData) {
        if (mFilterMenuPopView != null) {
            mFilterMenuPopView.setData(null);
            mFilterMenuPopView.hide();
        }
    }

    @Override
    public void onDismiss() {
        for (FilterMenuView menu : mFilterMenuViewList) {
            menu.setMenuSelected(false);
        }
    }

    public static class MenuItemData {
        public String id;
        public String title;
        public boolean moreOptions;
    }
}
