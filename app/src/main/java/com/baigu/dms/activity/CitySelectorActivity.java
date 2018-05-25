package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.baigu.dms.R;
import com.baigu.dms.common.view.CitySelectorView;
import com.baigu.dms.domain.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CitySelectorActivity extends BaseActivity implements CitySelectorView.OnCitySelectListener, ViewPager.OnPageChangeListener {
    private List<CitySelectorView> mCitySelectorViewList = new ArrayList<>();
    private CityFragmentPagerAdapter mCityFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selector);
        initToolBar();
        setTitle(R.string.city_select);
        initView();
        initData();
    }

    private void initView() {
        TabLayout tabLayout = findView(R.id.tabLayout);
        mViewPager = findView(R.id.viewPager);
        mCityFragmentPagerAdapter = new CityFragmentPagerAdapter();
        mViewPager.setAdapter(mCityFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {
        CitySelectorView view = new CitySelectorView(CitySelectorActivity.this);
        view.setOnCitySelectListener(this);
        mCitySelectorViewList.add(view);
        mCityFragmentPagerAdapter.notifyDataSetChanged();
        view.setParentCity(null);
        view.refresh();
    }

    class CityFragmentPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CitySelectorView view = mCitySelectorViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mCitySelectorViewList.get(position));
        }

        @Override
        public int getCount() {
            return mCitySelectorViewList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CitySelectorView view = mCitySelectorViewList.get(position);
            return view.getTitle();
        }
    }

    @Override
    public void onPageSelected(final int position) {
        mCitySelectorViewList.get(position).refresh();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCitySelected(City city) {
        if (city == null) return;
        final int areaType = city.getType();
        if (mCitySelectorViewList.size() <= areaType) {
            final CitySelectorView view = new CitySelectorView(CitySelectorActivity.this);
            boolean lastCity = view.setParentCity(city);
            if (lastCity) {
                setupResultIntent();
                finish();
                return;
            }
            view.setOnCitySelectListener(this);
            mCitySelectorViewList.add(view);
        } else {
            final CitySelectorView view = mCitySelectorViewList.get(areaType);
            boolean lastCity = view.setParentCity(city);
            if (lastCity) {
                setupResultIntent();
                finish();
                return;
            }
            for (int i = mCitySelectorViewList.size() - 1; i > areaType; i--) {
                mCitySelectorViewList.remove(i);
            }
        }
        mCityFragmentPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(areaType);
    }

    private void setupResultIntent() {
        Intent intent = getIntent();
        if (mCitySelectorViewList != null && mCitySelectorViewList.size() > 0) {
            StringBuilder sbCity = new StringBuilder();
            for (CitySelectorView csv : mCitySelectorViewList) {
                sbCity.append(csv.getSelectedCity().getName());
            }
            City city = mCitySelectorViewList.get(mCitySelectorViewList.size() - 1).getSelectedCity();
            intent.putExtra("city", city);

            ArrayList<City> cities = new ArrayList<>();
            for (CitySelectorView csv : mCitySelectorViewList) {
                cities.add(csv.getSelectedCity());
            }
            intent.putExtra("cityList", cities);
            intent.putExtra("allCityStr", sbCity.toString());
        }
        setResult(RESULT_OK, intent);
    }
}
