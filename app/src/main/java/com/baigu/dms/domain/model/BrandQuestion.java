package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Description 品牌问答
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/23 22:56
 */
public class BrandQuestion implements Parcelable {

    private String id;
    private String brandTitle;
    private String brandContent;
    private String brandBrief;
    private String createTime;
    private int sort;

    protected BrandQuestion(Parcel in) {
        id = in.readString();
        brandTitle = in.readString();
        brandContent = in.readString();
        brandBrief = in.readString();
        createTime = in.readString();
        sort = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(brandTitle);
        dest.writeString(brandContent);
        dest.writeString(brandBrief);
        dest.writeString(createTime);
        dest.writeInt(sort);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandQuestion> CREATOR = new Creator<BrandQuestion>() {
        @Override
        public BrandQuestion createFromParcel(Parcel in) {
            return new BrandQuestion(in);
        }

        @Override
        public BrandQuestion[] newArray(int size) {
            return new BrandQuestion[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandTitle() {
        return brandTitle;
    }

    public void setBrandTitle(String brandTitle) {
        this.brandTitle = brandTitle;
    }

    public String getBrandContent() {
        return brandContent;
    }

    public void setBrandContent(String brandContent) {
        this.brandContent = brandContent;
    }

    public String getBrandBrief() {
        return brandBrief;
    }

    public void setBrandBrief(String brandBrief) {
        this.brandBrief = brandBrief;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
