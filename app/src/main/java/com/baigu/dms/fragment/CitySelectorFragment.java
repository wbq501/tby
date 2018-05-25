package com.baigu.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.CitySelectorAdapter;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.domain.model.City;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
public class CitySelectorFragment extends TabFragment implements OnRVItemClickListener {

    private CitySelectorAdapter mCitySelectorAdapter;
    private OnCitySelectListener mOnCitySelectListener;
    private City mSelectedCity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_selector, null);
        RecyclerView rvAddress = (RecyclerView) view.findViewById(R.id.rv_city);
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        mCitySelectorAdapter = new CitySelectorAdapter();
        mCitySelectorAdapter.setOnItemClickListener(this);
        rvAddress.setAdapter(mCitySelectorAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<City> data = bundle.getParcelableArrayList("data");
            mCitySelectorAdapter.setData(data);
            mCitySelectorAdapter.notifyDataSetChanged();
        }
        return view;
    }

    @Override
    public String getTitle() {
        mTitle = mSelectedCity == null ? BaseApplication.getContext().getString(R.string.pls_select) : mSelectedCity.getName();
        return mTitle;
    }

    public void setOnCitySelectListener(OnCitySelectListener listener) {
        mOnCitySelectListener = listener;
    }

    public void setData(List<City> list) {
        if (list == null) {
            mSelectedCity = null;
        }
        if (mCitySelectorAdapter != null) {
            mCitySelectorAdapter.setData(list);
            mCitySelectorAdapter.notifyDataSetChanged();
        }
    }

    public interface OnCitySelectListener {
        void onCitySelected(City city);
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        if (mOnCitySelectListener != null) {
            mSelectedCity = mCitySelectorAdapter.getItem(position);
            mOnCitySelectListener.onCitySelected(mSelectedCity);
        }
    }
}
