package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.CitySelectorAdapter;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.fragment.CitySelectorFragment;
import com.baigu.dms.presenter.CitySelectorPresenter;
import com.baigu.dms.presenter.impl.CitySelectorPresenterImpl;
import com.micky.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class CitySelectorView extends FrameLayout implements OnRVItemClickListener{

    private CitySelectorAdapter mCitySelectorAdapter;
    private OnCitySelectListener mOnCitySelectListener;
    private City mSelectedCity;
    private City mParentCity;
    private CitySelectorPresenter mCitySelectorPresenter;
    private List<City> mCityList;

    private String mTitle;

    public CitySelectorView(Context context) {
        super(context);
        initView();
    }

    public CitySelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CitySelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_city_selector, this);
        RecyclerView rvAddress = (RecyclerView) findViewById(R.id.rv_city);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        mCitySelectorAdapter = new CitySelectorAdapter();
        mCitySelectorAdapter.setOnItemClickListener(this);
        rvAddress.setAdapter(mCitySelectorAdapter);
        mCitySelectorPresenter = new CitySelectorPresenterImpl(null, null);
    }

    public String getTitle() {
        mTitle = mSelectedCity == null ? BaseApplication.getContext().getString(R.string.pls_select) : mSelectedCity.getName();
        return mTitle;
    }

    public City getSelectedCity() {
        return mSelectedCity;
    }

    public void setOnCitySelectListener(OnCitySelectListener listener) {
        mOnCitySelectListener = listener;
    }

    public boolean setParentCity(City city) {
        mParentCity = city;
        mSelectedCity = null;
        mCitySelectorAdapter.setCitySelected(null);
        String parentId = mParentCity == null ? "" : mParentCity.getId();
        boolean noChildCity = false;
        try {
//            CityRepository repository = RepositoryFactory.getInstance().getCityRepository();
//            mCityList = TextUtils.isEmpty(parentId) ? repository.queryByAreaType(1) : repository.queryByParentId(parentId);
            mCityList = mCitySelectorPresenter.loadCity(parentId);
            noChildCity = mParentCity != null && mCityList != null && mCityList.size() == 0;
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return noChildCity;
    }

    public void refresh() {
        mCitySelectorAdapter.setData(mCityList);
        mCitySelectorAdapter.notifyDataSetChanged();
    }

    public interface OnCitySelectListener {
        void onCitySelected(City city);
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        if (mOnCitySelectListener != null) {
            mSelectedCity = mCitySelectorAdapter.getItem(position);
            mCitySelectorAdapter.setCitySelected(mSelectedCity);
            mOnCitySelectListener.onCitySelected(mSelectedCity);
        }
    }

}
