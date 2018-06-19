package com.baigu.dms.domain.netservice.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.baigu.dms.domain.model.OrderDetail;
import com.baigu.dms.domain.model.OrderDetailGoods;
import com.baigu.dms.domain.model.OrderGoods;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 12:19
 */
public class OrderDetailResult implements Parcelable {
    private String id;
    private String orderNo;
    private String createTime;
    private String createDate;
    private String consigneeName;
    private String expressName;
    private String expressPrice;
    private String status;
    private String payMode;
    private String goodsPrice;
    private String totalPrice;
    private String actualPrice;
    private String discountPrice;
    private String consigneePhone;
    private String consigneeAddress;
    private OrderDetail order;
    private List<OrderGoods> goodsList;
    private  String remark;
    private String refundReason;
    private List<String> logisticsNos;

    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(String expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getLogisticsNos() {
        return logisticsNos;
    }

    public void setLogisticsNos(List<String> logisticsNos) {
        this.logisticsNos = logisticsNos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.order, flags);
        dest.writeTypedList(this.goodsList);
        dest.writeString(this.createTime);
        dest.writeString(this.orderNo);
        dest.writeString(this.totalPrice);
        dest.writeString(this.payMode);
        dest.writeString(this.status);
        dest.writeString(this.actualPrice);
        dest.writeString(this.discountPrice);
    }

    public OrderDetailResult() {
    }

    protected OrderDetailResult(Parcel in) {
        this.order = in.readParcelable(OrderDetail.class.getClassLoader());
        this.goodsList = in.createTypedArrayList(OrderGoods.CREATOR);
        this.createTime = in.readString();
        this.orderNo = in.readString();
        this.totalPrice = in.readString();
        this.actualPrice = in.readString();
        this.discountPrice = in.readString();
        this.payMode = in.readString();
        this.status=in.readString();
    }

    public static final Creator<OrderDetailResult> CREATOR = new Creator<OrderDetailResult>() {
        @Override
        public OrderDetailResult createFromParcel(Parcel source) {
            return new OrderDetailResult(source);
        }

        @Override
        public OrderDetailResult[] newArray(int size) {
            return new OrderDetailResult[size];
        }
    };
}
