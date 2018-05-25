package com.baigu.dms.presenter;

import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.netservice.common.model.PageResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface AddressPresenter extends BasePresenter {

    void loadAddress(int pageNum, boolean showDialog);
    void saveOrUpdateAddress(Address address);
    void deleteAddress(Address address);
    Observable<RxOptional<List<City>>> loadCityList(final String cityId);

    interface AddressView {
        void onLoadAddress(PageResult<Address> addressPageResult);
        void onSaveOrUpdateAddress(Address address, boolean result);
        void onDeleteAddress(Address address, boolean b);
    }
}
