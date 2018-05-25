package com.baigu.dms.common.utils;

import android.content.Context;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.Order;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 21:41
 */
public class OrderUtils {

    public static  final int UNPAY=0;//待付款
    public static  final int UNDELIVER=10;//待发货
    public static  final int DELIVER=20;//已发货
    public static  final int COMPLETE=30;//已收货
    public static  final int REFUND_APPLY_FOR=60;//申请退款中
    public static  final int REFUND_APPLY=70;//退款中
    public static  final int CANCELED=-10;//取消
    public static  final int REFUND=90;//退款/退货
    public static  final int REFUNDED=80;//已退款

    /**获取订单状态对应的字符串*/
    public static String getStatusLabel(Context context, int status) {
        switch (status){
            case Order.Status.UNPAY:
                status=UNPAY;
                break;
            case Order.Status.DELIVERED:
                status=DELIVER;
                break;
            case Order.Status.UNDELIVER:
                status=UNDELIVER;
                break;
//            case Order.Status.CANCELED:
//                status=COMPLETE;
//                break;
//            case Order.Status.REFUND_APPLY:
//                status=REFUND_APPLY_FOR;
//                break;
//            case Order.Status.REFUND_APPLY:
//                status=REFUND_APPLY;
//                break;
            case Order.Status.CANCELED:
                status=CANCELED;
                break;
        }
        String statusLabel = "";
        switch (status) {
            case UNPAY:
                statusLabel = context.getString(R.string.unpay);
                break;
            case UNDELIVER:
                statusLabel = context.getString(R.string.undeliver);
                break;
            case DELIVER:
                statusLabel = context.getString(R.string.delivered);
                break;
            case COMPLETE:
                statusLabel = context.getString(R.string.complete);
                break;
            case CANCELED:
                statusLabel = context.getString(R.string.canceled);
                break;
            case REFUND:
                statusLabel = context.getString(R.string.refund);
                break;

            case REFUNDED:
                statusLabel = context.getString(R.string.refunded);
                break;
            case REFUND_APPLY:
                statusLabel = context.getString(R.string.refund_applyed);
                break;
            case REFUND_APPLY_FOR:
                statusLabel = context.getString(R.string.refund_apply);
                break;
            default:
                break;
        }
        return statusLabel;
    }

    /**获取订单状态对应的图片资源*/
    public static int getStatusBgResId(Context context, int status) {
        int resId = R.mipmap.order_unpay;
        switch (status) {
            case OrderUtils.UNPAY:
                resId = R.mipmap.order_unpay;
                break;
            case OrderUtils.UNDELIVER:
                resId = R.mipmap.order_preparing;
                break;
            case OrderUtils.DELIVER:
                resId = R.mipmap.order_delivered;
                break;
            case OrderUtils.REFUND_APPLY_FOR:
                resId = R.mipmap.order_refunded;
                break;
            case OrderUtils.REFUND_APPLY:
                resId = R.mipmap.order_refunded;
                break;
            case OrderUtils.REFUNDED:
                resId = R.mipmap.order_refunded;
                break;

            case OrderUtils.CANCELED:
                resId = R.mipmap.order_canceled;
                break;
            default:
                break;
        }
        return resId;
    }
}
