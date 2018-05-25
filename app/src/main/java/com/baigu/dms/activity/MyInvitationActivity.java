package com.baigu.dms.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.fragment.BaseFragment;
import com.baigu.dms.fragment.MyInvitationFragment;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class MyInvitationActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyInvitationFragment mMyInviteVerifiedFragment;
    private MyInvitationFragment mMyInviteUnVerifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitation);
        initToolBar();
        setTitle(R.string.my_invitation);
        initView();
    }

    private void initView() {

        mMyInviteVerifiedFragment = new MyInvitationFragment();
        mMyInviteUnVerifyFragment = new MyInvitationFragment();
        mMyInviteUnVerifyFragment.setUnVerfy(true);

        mViewPager = findView(R.id.view_pager);
        TabPagerAdapter tabPagerAdater = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabPagerAdater);

        mTabLayout = findView(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.main_text), getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(ViewUtils.dip2px(2.0f));
    }


    public class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = null;
            if (position == 0) {
                fragment = mMyInviteVerifiedFragment;
            } else {
                fragment = mMyInviteUnVerifyFragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.completed);
                case 1:
                    return getString(R.string.unverify);
            }
            return null;
        }
    }
}
