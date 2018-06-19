package com.baigu.dms.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.FragmentPageManager;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ShareToQQ;
import com.baigu.dms.common.utils.ShareUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.TabButton;
import com.baigu.dms.common.view.arcmenu.ArcMenu;
import com.baigu.dms.common.view.circleimage.transfer.transfer.Transferee;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.DBCore;
import com.baigu.dms.fragment.DiscoverFragment;
import com.baigu.dms.fragment.HomeFragment;
import com.baigu.dms.fragment.MyFragment;
import com.baigu.dms.fragment.ShopFragment;
import com.baigu.dms.fragment.TabFragment;
import com.baigu.dms.wxapi.WXShare;
import com.hyphenate.chat.Message;
import com.micky.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, FragmentPageManager.FragmentPageListener {

    private TabButton mTabHome;
    private TabButton mTabDiscover;
    private TabButton mTabMessage;
    private TabButton mTabMy;

    private View mLayoutDim;


    private ArcMenu mArcMenu;

    public FragmentPageManager mFragmentPageManager;

    private List<TabFragment> mFragmentList = new ArrayList<TabFragment>();

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getDefault().register(this);
        WXShare.getInstance().register();
        initView();
    }

    private void initView() {
        initPagerView();

        mTabHome = findView(R.id.tab_home);
        mTabHome.setChecked(true);
        mTabHome.setOnClickListener(this);

        mTabDiscover = findView(R.id.tab_shop);
        mTabDiscover.setOnClickListener(this);

        mTabMessage = findView(R.id.tab_discover);
        mTabMessage.setOnClickListener(this);

        mTabMy = findView(R.id.tab_my);
        mTabMy.setOnClickListener(this);

        initArcMenu();
    }

    private void initPagerView() {

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setTabIndex(mFragmentList.size());

        ShopFragment shopFragment = new ShopFragment();
        shopFragment.setTabIndex(mFragmentList.size());

        DiscoverFragment discoverFragment = new DiscoverFragment();
        discoverFragment.setTabIndex(mFragmentList.size());

        MyFragment myFragment = new MyFragment();
        myFragment.setTabIndex(mFragmentList.size());

        mFragmentList.add(homeFragment);
        mFragmentList.add(shopFragment);
        mFragmentList.add(discoverFragment);
        mFragmentList.add(myFragment);

        mFragmentPageManager = new FragmentPageManager(this, mFragmentList, R.id.main_container);
        mFragmentPageManager.setPageListener(this);
        mFragmentPageManager.selectPage(0);
    }

    private void initArcMenu() {
        mArcMenu = findView(R.id.arc_menu);
        final int[] itemDrawables = {R.mipmap.share_weixin, R.mipmap.share_weixin_circle, R.mipmap.share_qq, R.mipmap.share_qq_zone};
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            mArcMenu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (ViewUtils.isFastClick()) return;
                    switch (position) {
                        case 0:
                            WXShare.getInstance().shareText(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl(), false);
                            break;
                        case 1:
                            WXShare.getInstance().shareText(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl(), true);
                            break;
                        case 2:
                            ShareToQQ.getInstance(MainActivity.this).shareTextToFriend(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl());
                            break;
                        case 3:
                            ShareToQQ.getInstance(MainActivity.this).shareTextToZone(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl());
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        mArcMenu.setOnArcMenuExpandListener(new ArcMenu.OnArcMenuExpandListener() {
            @Override
            public void onExpand(boolean b) {
                mLayoutDim.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        mLayoutDim = findView(R.id.layout_dim);
        mLayoutDim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mArcMenu.setExpand(false);
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mTabHome.setText(R.string.home);
        mTabDiscover.setText(R.string.shop);
        mTabMessage.setText(R.string.discover);
        mTabMy.setText(R.string.my);

        int selectedPosition = mFragmentPageManager.getSeletedIndex() == -1 ? 0 : mFragmentPageManager.getSeletedIndex();
        TabButton tabButton = getTabButton(selectedPosition);
        onClick(tabButton);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        Transferee.destroy();
        RxBus.getDefault().unregister(this);
        WXShare.getInstance().unregister();
        super.onDestroy();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onClick(View v) {
        resetTabChecked();
        switch (v.getId()) {
            case R.id.tab_home:
            case R.id.tab_shop:
            case R.id.tab_discover:
            case R.id.tab_my:
                TabButton tabButton = ((TabButton) v);
                tabButton.setChecked(true);
                mFragmentPageManager.selectPage(tabButton.getPosition());
                break;
        }
    }

    private void resetTabChecked() {
        mTabHome.setChecked(false);
        mTabDiscover.setChecked(false);
        mTabMessage.setChecked(false);
        mTabMy.setChecked(false);
    }

    private TabButton getTabButton(int position) {
        TabButton tabButton = mTabHome;
        switch (position) {
            case 0:
                tabButton = mTabHome;
                break;
            case 1:
                tabButton = mTabDiscover;
                break;
            case 2:
                tabButton = mTabMessage;
                break;
            case 3:
                tabButton = mTabMy;
                break;
            default:
                break;
        }
        return tabButton;
    }

    @Override
    public void onPageSelected(int index) {
        for (TabFragment fragment : mFragmentList) {
            if (fragment.getTabIndex() == index) {
                fragment.onTabChecked(true);
            } else {
                fragment.onTabChecked(false);
            }
        }
        if (mArcMenu != null) {
            mArcMenu.setExpand(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ViewUtils.showToastInfo(R.string.exit_twice);
                mExitTime = System.currentTimeMillis();
            } else {
                SPUtils.clearBuyType();
                ViewUtils.exitApp(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
