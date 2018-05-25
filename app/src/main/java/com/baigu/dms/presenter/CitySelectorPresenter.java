package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.City;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface CitySelectorPresenter extends BasePresenter {

    List<City> loadCity(String parentId);

    interface CityView {
        void onLoadCity(List<City> list);
    }
}
