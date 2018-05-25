package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description 订单列表订单里的商品
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/06 15:01
 */
public class OrderGoods implements Parcelable {

    private String productId;
    private double uniformPrice;
    private double agentPrice;
    private String productName;
    private String goodsImg;
    private int goodsNum;
    /**第几个商品*/
    private int index;

    public double getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(double agentPrice) {
        this.agentPrice = agentPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getUniformPrice() {
        return uniformPrice;
    }

    public void setUniformPrice(double uniformPrice) {
        this.uniformPrice = uniformPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public OrderGoods() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeDouble(this.uniformPrice);
        dest.writeString(this.productName);
        dest.writeString(this.goodsImg);
        dest.writeInt(this.goodsNum);
        dest.writeInt(this.index);
    }

    protected OrderGoods(Parcel in) {
        this.productId = in.readString();
        this.uniformPrice = in.readDouble();
        this.productName = in.readString();
        this.goodsImg = in.readString();
        this.goodsNum = in.readInt();
        this.index = in.readInt();
    }

    public static final Creator<OrderGoods> CREATOR = new Creator<OrderGoods>() {
        @Override
        public OrderGoods createFromParcel(Parcel source) {
            return new OrderGoods(source);
        }

        @Override
        public OrderGoods[] newArray(int size) {
            return new OrderGoods[size];
        }
    };
}
