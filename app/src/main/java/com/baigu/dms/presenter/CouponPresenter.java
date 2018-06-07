package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Coupon;

import java.util.List;

public interface CouponPresenter extends BasePresenter{
    void getCouponList(int pageNum,int status);

    interface CouponView{
        void CouponList(Coupon coupon);
    }
}
