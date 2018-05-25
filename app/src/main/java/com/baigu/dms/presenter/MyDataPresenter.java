package com.baigu.dms.presenter;

import com.baigu.dms.domain.netservice.common.model.MyDataResult;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface MyDataPresenter extends BasePresenter {

    void getMyData();

    interface MyDataView {
        void onGetMyData(MyDataResult result);
    }
}
