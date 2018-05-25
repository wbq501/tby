package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description 订单详情里的商品
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 11:59
 */
public class OrderDetailGoods implements Parcelable {

    private String ids;
    private String orderids;
    private double uniformprice;
    private String goodsids;
    private String goodsimg;
    private String goodsname;
    private int goodsnum;
    private String createtime;
    private String updatetime;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getOrderids() {
        return orderids;
    }

    public void setOrderids(String orderids) {
        this.orderids = orderids;
    }

    public double getUniformprice() {
        return uniformprice;
    }

    public void setUniformprice(double uniformprice) {
        this.uniformprice = uniformprice;
    }

    public String getGoodsids() {
        return goodsids;
    }

    public void setGoodsids(String goodsids) {
        this.goodsids = goodsids;
    }

    public String getGoodsimg() {
        return goodsimg;
    }

    public void setGoodsimg(String goodsimg) {
        this.goodsimg = goodsimg;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(int goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.orderids);
        dest.writeDouble(this.uniformprice);
        dest.writeString(this.goodsids);
        dest.writeString(this.goodsimg);
        dest.writeString(this.goodsname);
        dest.writeInt(this.goodsnum);
        dest.writeString(this.createtime);
        dest.writeString(this.updatetime);
    }

    public OrderDetailGoods() {
    }

    protected OrderDetailGoods(Parcel in) {
        this.ids = in.readString();
        this.orderids = in.readString();
        this.uniformprice = in.readDouble();
        this.goodsids = in.readString();
        this.goodsimg = in.readString();
        this.goodsname = in.readString();
        this.goodsnum = in.readInt();
        this.createtime = in.readString();
        this.updatetime = in.readString();
    }

    public static final Creator<OrderDetailGoods> CREATOR = new Creator<OrderDetailGoods>() {
        @Override
        public OrderDetailGoods createFromParcel(Parcel source) {
            return new OrderDetailGoods(source);
        }

        @Override
        public OrderDetailGoods[] newArray(int size) {
            return new OrderDetailGoods[size];
        }
    };
}
