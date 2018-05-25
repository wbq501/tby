package com.baigu.dms.common.view.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/15 23:01
 */
public class GalleryImageBean implements Parcelable {
    protected List<String> urlList;

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public List<String> getUrlList() {
        return urlList;
    }


    public GalleryImageBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.urlList);
    }

    protected GalleryImageBean(Parcel in) {
        this.urlList = in.createStringArrayList();
    }

    public static final Creator<GalleryImageBean> CREATOR = new Creator<GalleryImageBean>() {
        @Override
        public GalleryImageBean createFromParcel(Parcel source) {
            return new GalleryImageBean(source);
        }

        @Override
        public GalleryImageBean[] newArray(int size) {
            return new GalleryImageBean[size];
        }
    };
}
