package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description 商品分类
 * 
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 23:06
 */
public class GoodsCategory implements Parcelable {
    private String id;
    private int sort;
    private String describe;
    private String name;
    private String img;
    private boolean hot;
    private int number = 0;
//    private boolean nocheck;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public String isId() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GoodsCategory() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
//        dest.writeByte(this.isParent ? (byte) 1 : (byte) 0);
//        dest.writeString(this.children);
        dest.writeString(this.name);
//        dest.writeString(this.icon);
//        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
//        dest.writeByte(this.nocheck ? (byte) 1 : (byte) 0);
    }

    protected GoodsCategory(Parcel in) {
        this.id = in.readString();
//        this.isParent = in.readByte() != 0;
//        this.children = in.readString();
        this.name = in.readString();
//        this.icon = in.readString();
//        this.checked = in.readByte() != 0;
//        this.nocheck = in.readByte() != 0;
    }

    public static final Creator<GoodsCategory> CREATOR = new Creator<GoodsCategory>() {
        @Override
        public GoodsCategory createFromParcel(Parcel source) {
            return new GoodsCategory(source);
        }

        @Override
        public GoodsCategory[] newArray(int size) {
            return new GoodsCategory[size];
        }
    };
}
