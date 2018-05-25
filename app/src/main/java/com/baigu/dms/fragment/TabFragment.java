package com.baigu.dms.fragment;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 21:37
 */
public class TabFragment extends BaseFragment {

    protected int mTabIndex;
    protected String mTitle;

    public int getTabIndex() {
        return mTabIndex;
    }

    public void setTabIndex(int index) {
        this.mTabIndex = index;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void onTabChecked(boolean selected) {

    }
}
