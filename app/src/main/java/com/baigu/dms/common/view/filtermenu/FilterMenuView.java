package com.baigu.dms.common.view.filtermenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.baigu.dms.R;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/30 0:35
 */
public class FilterMenuView extends LinearLayout {
    private CheckBox mCheckBox;
    private int mPadding = 0;
    private OnMenuSelectedListener mOnMenuSelectedListener;
    private ExpandFilterMenuView.MenuItemData mMenuItemData;

    public FilterMenuView(Context context) {
        super(context);
        initView();
    }

    public FilterMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FilterMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public FilterMenuView(Context context, int padding) {
        super(context);
        mPadding = padding;
        initView();
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    public void setOnMenuSelectedListener(OnMenuSelectedListener menuSelectedListener) {
        mOnMenuSelectedListener = menuSelectedListener;
    }

    private void initView() {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.view_filter_menu, null);
        view.setBackground(getResources().getDrawable(R.drawable.bg_filter_menu_inner_normal));
        mCheckBox = (CheckBox) view.findViewById(R.id.cb);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, params);
        params.bottomMargin = mPadding * 2;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckBox.setChecked(!mCheckBox.isChecked());
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnMenuSelectedListener != null) {
                    if (mOnMenuSelectedListener.onOtherShowed(mMenuItemData)) {
                        return;
                    }
                }
                if (mCheckBox.isChecked()) {
                    setBackground(getResources().getDrawable(R.drawable.bg_filter_menu_inner_sel));
                    view.setBackground(null);

                    if (mOnMenuSelectedListener != null) {
                        mOnMenuSelectedListener.onSelected(mMenuItemData);
                    }
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_filter_menu_inner_normal));
                    setBackground(null);
                    if (mOnMenuSelectedListener != null) {
                        mOnMenuSelectedListener.onUnSelected(mMenuItemData);
                    }
                }
            }
        });
    }

    public void setMenuItemData(ExpandFilterMenuView.MenuItemData menuItemData) {
        mMenuItemData = menuItemData;
        mCheckBox.setText(menuItemData.title);
    }

    public void setMenuSelected(boolean b) {
        mCheckBox.setChecked(b);
    }

    public interface OnMenuSelectedListener {
        boolean onOtherShowed(ExpandFilterMenuView.MenuItemData tabData);
        void onSelected(ExpandFilterMenuView.MenuItemData tabData);
        void onUnSelected(ExpandFilterMenuView.MenuItemData tabData);
    }
}
