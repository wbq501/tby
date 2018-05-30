package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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
    private Boolean isShow = true;

    public static final class StockShowType {
        public static final int SHOW = 1;
        public static final int NOT_SHOW = 2;
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

    public String getSupercoverpath() {
        return supercoverpath;
    }

    public void setSupercoverpath(String supercoverpath) {
        this.supercoverpath = supercoverpath;
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

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc;
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

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
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

    public GoodsCategory getCategory() {
        return category;
    }

    public void setCategory(GoodsCategory category) {
        this.category = category;
    }

    public List<ShopPictrue> getPics() {
        return pics;
    }

    public void setPics(List<ShopPictrue> pics) {
        this.pics = pics;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "ids='" + ids + '\'' +
                ", coverpath='" + coverpath + '\'' +
                ", supercoverpath='" + supercoverpath + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsdesc='" + goodsdesc + '\'' +
                ", goodsdetail='" + goodsdetail + '\'' +
                ", goodsdetailpath='" + goodsdetailpath + '\'' +
                ", marketprice=" + marketprice +
                ", uniformprice=" + uniformprice +
                ", stocknum=" + stocknum +
                ", goodsweight=" + goodsweight +
                ", skus=" + skus +
                ", buyNum=" + buyNum +
                ", orderflag=" + orderflag +
                ", isshow=" + isshow +
                ", limitnum=" + limitnum +
                ", category=" + category +
                ", pics=" + pics +
                ", isShow=" + isShow +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.coverpath);
        dest.writeString(this.supercoverpath);
        dest.writeString(this.goodsname);
        dest.writeString(this.goodsdesc);
        dest.writeString(this.goodsdetail);
        dest.writeString(this.goodsdetailpath);
        dest.writeDouble(this.marketprice);
        dest.writeDouble(this.uniformprice);
        dest.writeInt(this.stocknum);
        dest.writeLong(this.goodsweight);
        dest.writeTypedList(this.skus);
        dest.writeInt(this.buyNum);
        dest.writeInt(this.orderflag);
        dest.writeInt(this.isshow);
        dest.writeInt(this.limitnum);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(isShow ? 1 : -1);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.ids = in.readString();
        this.coverpath = in.readString();
        this.supercoverpath = in.readString();
        this.goodsname = in.readString();
        this.goodsdesc = in.readString();
        this.goodsdetail = in.readString();
        this.goodsdetailpath = in.readString();
        this.marketprice = in.readDouble();
        this.uniformprice = in.readDouble();
        this.stocknum = in.readInt();
        this.goodsweight = in.readLong();
        this.skus = in.createTypedArrayList(Sku.CREATOR);
        this.buyNum = in.readInt();
        this.orderflag = in.readInt();
        this.isshow = in.readInt();
        this.limitnum = in.readInt();
        this.category = in.readParcelable(GoodsCategory.class.getClassLoader());
        this.isShow = in.readInt() > 0 ? true:false;
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
