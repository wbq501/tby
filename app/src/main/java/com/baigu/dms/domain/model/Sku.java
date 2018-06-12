package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class Sku implements Parcelable{
    private String skuId;
    private double  uniformprice;
    private double  marketprice;
    private long goodsweight;
    private String skuAttr;
    private  int stocknum;
    private int buyNum;
    private int number = 0;//购买数量
    private  boolean isDefault;

    private String minPrice;
    private String maxPrice;
    private String group;
    private int maxCount;
    private String expressGroups;

    private Boolean isShow = true;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getStocknum() {
        return stocknum;
    }

    public void setStocknum(int stocknum) {
        this.stocknum = stocknum;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public double getUniformprice() {
        return uniformprice;
    }

    public void setUniformprice(double uniformprice) {
        this.uniformprice = uniformprice;
    }


    public long getGoodsweight() {
        return goodsweight;
    }

    public void setGoodsweight(long goodsweight) {
        this.goodsweight = goodsweight;
    }

    public String getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(String skuAttr) {
        this.skuAttr = skuAttr;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public double getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(double marketprice) {
        this.marketprice = marketprice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getExpressGroups() {
        return expressGroups;
    }

    public void setExpressGroups(String expressGroups) {
        this.expressGroups = expressGroups;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "skuId='" + skuId + '\'' +
                ", uniformprice=" + uniformprice +
                ", marketprice=" + marketprice +
                ", goodsweight=" + goodsweight +
                ", skuAttr='" + skuAttr + '\'' +
                ", stocknum=" + stocknum +
                ", buyNum=" + buyNum +
                ", number=" + number +
                ", isDefault=" + isDefault +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", group='" + group + '\'' +
                ", maxCount=" + maxCount +
                ", expressGroups='" + expressGroups + '\'' +
                ", isShow=" + isShow +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.skuId);
        dest.writeDouble(this.uniformprice);
        dest.writeDouble(this.marketprice);
        dest.writeLong(this.goodsweight);
        dest.writeString(this.skuAttr);
        dest.writeInt(this.stocknum);
        dest.writeInt(this.buyNum);
        dest.writeInt(this.number);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeString(this.minPrice);
        dest.writeString(this.maxPrice);
        dest.writeString(this.group);
        dest.writeInt(this.maxCount);
        dest.writeString(this.expressGroups);
        dest.writeInt(isShow ? 1 : -1);
    }

    public Sku() {
    }

    protected Sku(Parcel in) {
        this.skuId = in.readString();
        this.uniformprice = in.readDouble();
        this.marketprice = in.readDouble();
        this.goodsweight = in.readLong();
        this.skuAttr = in.readString();
        this.stocknum = in.readInt();
        this.buyNum = in.readInt();
        this.number = in.readInt();
        this.isDefault = in.readByte() != 0;
        this.minPrice = in.readString();
        this.maxPrice = in.readString();
        this.group = in.readString();
        this.maxCount = in.readInt();
        this.expressGroups = in.readString();
        this.isShow = in.readInt() > 0 ? true:false;
    }

    public static final Creator<Sku> CREATOR = new Creator<Sku>() {
        @Override
        public Sku createFromParcel(Parcel source) {
            return new Sku(source);
        }

        @Override
        public Sku[] newArray(int size) {
            return new Sku[size];
        }
    };
}
