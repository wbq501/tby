package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Express;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface ExpressSelectorPresenter extends BasePresenter {

    void loadExpress();

    interface ExpressView {
        void onLoadExpress(List<Express> list);
    }
}
