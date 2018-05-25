package com.baigu.dms.common.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.baigu.dms.fragment.TabFragment;

import java.util.List;

public class FragmentPageManager {

    private List<TabFragment> mFragmentList;
    private FragmentActivity mFragmentActivity;
    private int mContainerId;
    private int mSelectedIndex = -1;
    private FragmentPageListener mPageListener;

    public FragmentPageManager(FragmentActivity fragmentActivity, List<TabFragment> fragmentList, int containerId) {
        mFragmentActivity = fragmentActivity;
        mFragmentList = fragmentList;
        mContainerId = containerId;
    }

    public void setPageListener(FragmentPageListener listener) {
        mPageListener = listener;
    }

    public void selectPage(int index) {
        if (index > mFragmentList.size() - 1 || index == mSelectedIndex)  {
            return;
        }
        if (index > mSelectedIndex) {
            updatePage(index);
        } else {
            updatePageWithReverse(index);
        }
        if (mPageListener != null) {
            mPageListener.onPageSelected(index);
        }
        mSelectedIndex = index;
    }

    private void updatePage(int index) {
        FragmentTransaction fragmentTransaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragmentList.size(); i++) {
            TabFragment fragment = mFragmentList.get(i);
            if (i == mSelectedIndex) {
                mFragmentList.get(i).onPause();
                fragmentTransaction.hide(fragment);
            }
            if (i == index) {
                if (fragment.isAdded()) {
                    fragment.onResume();
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(mContainerId, fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }

    private void updatePageWithReverse(int index) {
        FragmentTransaction fragmentTransaction = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        for (int i = mFragmentList.size() - 1; i >= 0; i--) {
            TabFragment fragment = mFragmentList.get(i);
            if (i == mSelectedIndex) {
                mFragmentList.get(i).onPause();
                fragmentTransaction.hide(fragment);
            }
            if (i == index) {
                if (fragment.isAdded()) {
                    fragment.onResume();
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(mContainerId, fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }

    public int getSeletedIndex() {
        return mSelectedIndex;
    }

    public TabFragment getSelectedFragment() {
        return mFragmentList.get(mSelectedIndex);
    }

    public static interface FragmentPageListener {
        public void onPageSelected(int index);
    }
}