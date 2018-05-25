package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.ExpressInfo;
import com.baigu.dms.domain.model.LogisticsInfo;

import java.util.ArrayList;
import java.util.List;

public interface ExpressGetPresenter extends BasePresenter{
    void getExpress(String expressnum);

    interface ExpressGetView{
        void loadExpress(LogisticsInfo<ExpressInfo> result);
    }
}
