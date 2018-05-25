package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 12:05
 */
public class OrderDetail implements Parcelable {

    /**
     * 订单id
     */
    private String ids;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县区
     */
    private String area;
    /**
     * 创建时间
     */
    private String createtime;
    /**
     * 优惠券id
     */
    private String discountids;
    /**
     * 优惠价格
     */
    private double discountprice;
    /**
     * 快递公司id
     */
    private String expressids;
    /**
     * 快递公司名称
     */
    private String expressname;
    /**
     * 快递单号
     */
    private String expressorder;
    /**
     * 快递金额
     */
    private double expressprice;
    /**
     * 商品总金额
     */
    private double goodsprice;
    /**
     * 用户id
     */
    private String memberids;
    /**
     * 订单号
     */
    private String ordernum;
    /**
     * 上级代理id
     */
    private String parentids;
    /**
     * 支付单号
     */
    private String payorderno;
    /**
     * 支付类型
     */
    private String paytype;
    /**
     * 支付时间
     */
    private String paydatetime;
    /**
     * 无用字段
     */
    private String pids;
    /**
     * 收货人
     */
    private String receiptpeople;
    /**
     * 收货人电话
     */
    private String receiptphone;
    /**
     * 收货地址
     */
    private String receiptaddress;
    /**
     * 备注
     */
    private String remark;
    /**
     * 订单状态
     */
    private int status;
    /**
     * 订单总金额
     */
    private double totalprice;
    /**
     * 更新时间
     */
    private String updatetime;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getDiscountids() {
        return discountids;
    }

    public void setDiscountids(String discountids) {
        this.discountids = discountids;
    }

    public double getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(double discountprice) {
        this.discountprice = discountprice;
    }

    public String getExpressids() {
        return expressids;
    }

    public void setExpressids(String expressids) {
        this.expressids = expressids;
    }

    public String getExpressname() {
        return expressname;
    }

    public void setExpressname(String expressname) {
        this.expressname = expressname;
    }

    public String getExpressorder() {
        return expressorder;
    }

    public void setExpressorder(String expressorder) {
        this.expressorder = expressorder;
    }

    public double getExpressprice() {
        return expressprice;
    }

    public void setExpressprice(double expressprice) {
        this.expressprice = expressprice;
    }

    public double getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getMemberids() {
        return memberids;
    }

    public void setMemberids(String memberids) {
        this.memberids = memberids;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getParentids() {
        return parentids;
    }

    public void setParentids(String parentids) {
        this.parentids = parentids;
    }

    public String getPayorderno() {
        return payorderno;
    }

    public void setPayorderno(String payorderno) {
        this.payorderno = payorderno;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPaydatetime() {
        return paydatetime;
    }

    public void setPaydatetime(String paydatetime) {
        this.paydatetime = paydatetime;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
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

    public String getReceiptaddress() {
        return receiptaddress;
    }

    public void setReceiptaddress(String receiptaddress) {
        this.receiptaddress = receiptaddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }


    public OrderDetail() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.createtime);
        dest.writeString(this.discountids);
        dest.writeDouble(this.discountprice);
        dest.writeString(this.expressids);
        dest.writeString(this.expressname);
        dest.writeString(this.expressorder);
        dest.writeDouble(this.expressprice);
        dest.writeDouble(this.goodsprice);
        dest.writeString(this.memberids);
        dest.writeString(this.ordernum);
        dest.writeString(this.parentids);
        dest.writeString(this.payorderno);
        dest.writeString(this.paytype);
        dest.writeString(this.paydatetime);
        dest.writeString(this.pids);
        dest.writeString(this.receiptpeople);
        dest.writeString(this.receiptphone);
        dest.writeString(this.receiptaddress);
        dest.writeString(this.remark);
        dest.writeInt(this.status);
        dest.writeDouble(this.totalprice);
        dest.writeString(this.updatetime);
    }

    protected OrderDetail(Parcel in) {
        this.ids = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.createtime = in.readString();
        this.discountids = in.readString();
        this.discountprice = in.readDouble();
        this.expressids = in.readString();
        this.expressname = in.readString();
        this.expressorder = in.readString();
        this.expressprice = in.readDouble();
        this.goodsprice = in.readDouble();
        this.memberids = in.readString();
        this.ordernum = in.readString();
        this.parentids = in.readString();
        this.payorderno = in.readString();
        this.paytype = in.readString();
        this.paydatetime = in.readString();
        this.pids = in.readString();
        this.receiptpeople = in.readString();
        this.receiptphone = in.readString();
        this.receiptaddress = in.readString();
        this.remark = in.readString();
        this.status = in.readInt();
        this.totalprice = in.readDouble();
        this.updatetime = in.readString();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
