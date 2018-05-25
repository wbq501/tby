package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Description 订单
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/06 15:01
 */
public class Order implements Parcelable {

    private String ids;
    private String id;
    private String ordernum;
    private double goodsprice;
    private String receiptpeople;
    private String receiptphone;
    private String orderNo;
    private double totalPrice;
    private double expressPrice;
    private String createTime;
    private String consigneePhone;
    private String consigneeName;
    private int status;
    private List<OrderGoods> goodsList;
    /**商品数量*/
    private int goodsNum;

    public static class Status {
        /**所有*/
        public static final int ALL = 0;
        /**未支付*/
        public static final int UNPAY = 1;
        /**已支付*/
//        public static final int PAYED = 2;
        /**已发货*/
        public static final int DELIVERED = 3;
        /**已退款*/
        public static final int REFUNDED = 4;
        /**备货中*/
        public static final int PREPARING = 5;
        /**退款中*/
        public static final int REFUND_APPLY = 6;
        /**已取消*/
        public static final int CANCELED = 7;
        /**待发货*/
        public static final int UNDELIVER = 2;
    }


    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public double getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptpeople() {
        return receiptpeople;
    }

    public void setReceiptpeople(String receiptpeople) {
        this.receiptpeople = receiptpeople;
    }

    public String getReceiptphone() {
        return receiptphone;
    }

    public void setReceiptphone(String receiptphone) {
        this.receiptphone = receiptphone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(double expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.id);
        dest.writeString(this.ordernum);
        dest.writeString(this.createTime);
        dest.writeDouble(this.goodsprice);
        dest.writeDouble(this.expressPrice);
        dest.writeDouble(this.totalPrice);
        dest.writeString(this.receiptpeople);
        dest.writeString(this.receiptphone);
        dest.writeInt(this.status);
        dest.writeTypedList(this.goodsList);
        dest.writeInt(this.goodsNum);
    }

    protected Order(Parcel in) {
        this.ids = in.readString();
        this.id = in.readString();
        this.ordernum = in.readString();
        this.createTime = in.readString();
        this.goodsprice = in.readDouble();
        this.expressPrice = in.readDouble();
        this.totalPrice = in.readDouble();
        this.receiptpeople = in.readString();
        this.receiptphone = in.readString();
        this.status = in.readInt();
        this.goodsList = in.createTypedArrayList(OrderGoods.CREATOR);
        this.goodsNum = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
