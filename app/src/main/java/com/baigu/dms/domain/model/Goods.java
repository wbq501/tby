package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/26 20:08
 */
public class Goods implements Parcelable {
    private String ids;
    private String coverpath;
    private String supercoverpath;//仅首页
    private String goodsname;
    private String goodsdesc;
    private String goodsdetail;
    private String goodsdetailpath;
    private double marketprice;
    private double uniformprice;
    private int stocknum;
    private long goodsweight;
    private List<Sku> skus;
    private int buyNum = 0;
    private int orderflag = 0;
    private int isshow = StockShowType.NOT_SHOW;
    private int limitnum = 0;
    private GoodsCategory category;
    private List<ShopPictrue> pics;//列表显示position为1的图片

    protected Goods(Parcel in) {
        ids = in.readString();
        coverpath = in.readString();
        supercoverpath = in.readString();
        goodsname = in.readString();
        goodsdesc = in.readString();
        goodsdetail = in.readString();
        goodsdetailpath = in.readString();
        marketprice = in.readDouble();
        uniformprice = in.readDouble();
        stocknum = in.readInt();
        goodsweight = in.readLong();
        skus = in.createTypedArrayList(Sku.CREATOR);
        buyNum = in.readInt();
        orderflag = in.readInt();
        isshow = in.readInt();
        limitnum = in.readInt();
        category = in.readParcelable(GoodsCategory.class.getClassLoader());
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ids);
        parcel.writeString(coverpath);
        parcel.writeString(supercoverpath);
        parcel.writeString(goodsname);
        parcel.writeString(goodsdesc);
        parcel.writeString(goodsdetail);
        parcel.writeString(goodsdetailpath);
        parcel.writeDouble(marketprice);
        parcel.writeDouble(uniformprice);
        parcel.writeInt(stocknum);
        parcel.writeLong(goodsweight);
        parcel.writeTypedList(skus);
        parcel.writeInt(buyNum);
        parcel.writeInt(orderflag);
        parcel.writeInt(isshow);
        parcel.writeInt(limitnum);
        parcel.writeParcelable(category, i);
    }

    public static final class StockShowType {
        public static final int SHOW = 1;
        public static final int NOT_SHOW = 2;
    }

    public List<ShopPictrue> getPics() {
        return pics;
    }

    public void setPics(List<ShopPictrue> pics) {
        this.pics = pics;
    }

    public GoodsCategory getCategory() {
        return category;
    }

    public void setCategory(GoodsCategory category) {
        this.category = category;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCoverpath() {
        return coverpath;
    }

    public void setCoverpath(String coverpath) {
        this.coverpath = coverpath;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsdesc() {
        return goodsdesc;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc;
    }

    public double getUniformprice() {
        return uniformprice;
    }

    public void setUniformprice(double uniformprice) {
        this.uniformprice = uniformprice;
    }

    public int getStocknum() {
        return stocknum;
    }

    public void setStocknum(int stocknum) {
        this.stocknum = stocknum;
    }

    public long getGoodsweight() {
        return goodsweight;
    }

    public void setGoodsweight(long goodsweight) {
        this.goodsweight = goodsweight;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getGoodsdetail() {
        return goodsdetail;
    }

    public void setGoodsdetail(String goodsdetail) {
        this.goodsdetail = goodsdetail;
    }

    public String getGoodsdetailpath() {
        return goodsdetailpath;
    }

    public void setGoodsdetailpath(String goodsdetailpath) {
        this.goodsdetailpath = goodsdetailpath;
    }

    public double getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(double marketprice) {
        this.marketprice = marketprice;
    }

    public String getSupercoverpath() {
        return supercoverpath;
    }

    public void setSupercoverpath(String supercoverpath) {
        this.supercoverpath = supercoverpath;
    }

    public int getOrderflag() {
        return orderflag;
    }

    public void setOrderflag(int orderflag) {
        this.orderflag = orderflag;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getLimitnum() {
        return limitnum;
    }

    public void setLimitnum(int limitnum) {
        this.limitnum = limitnum;
    }

    public Goods() {
    }








    @Override
    public String toString() {
        return "Goods{" +
                "ids='" + ids + '\'' +
                ", coverpath='" + coverpath + '\'' +
                ", suppercoverpath='" + supercoverpath + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsdesc='" + goodsdesc + '\'' +
                ", goodsdetail='" + goodsdetail + '\'' +
                ", goodsdetailpath='" + goodsdetailpath + '\'' +
                ", uniformprice=" + uniformprice +
                ", stocknum=" + stocknum +
                ", goodsweight=" + goodsweight +
                ", skus=" + skus +
                ", buyNum=" + buyNum +
                ", orderflag=" + orderflag +
                ", isshow=" + isshow +
                ", limitnum=" + limitnum +
                ", category=" + category +
                '}';
    }
}
