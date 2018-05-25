package com.baigu.dms.domain.netservice.common.model;

import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.BrandQuestion;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/22 20:24
 */
public class MyDataResult {

//    public Advert cb;
//    /**已发货订单数*/
//    public int delivered;
//    /**apid*/
//    public int apid;
//    /**待发货订单数*/
//    public int backOrders;
//    /**已支付订单数*/
//    public int haveApid;
//    /**品牌问答列表*/
//    public List<BrandQuestion> cs;

    public Advert advertisVo;
    /**品牌问答列表*/
    public List<BrandQuestion> brandQuestionVos;
    //待付款
    public int waitPayCount;
    //已付款
    public int paidCount;
    //待发货
    public int waitSendCount;
    //已发货
    public int sentCount;
    //退款
    public int refundCount;
}
