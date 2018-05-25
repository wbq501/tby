package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpressList implements Parcelable {
    private String name;
    private String num;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.num);
    }

    public ExpressList() {
    }

    protected ExpressList(Parcel in) {
        this.name = in.readString();
        this.num = in.readString();
    }

    public static final Creator<ExpressList> CREATOR = new Creator<ExpressList>() {
        @Override
        public ExpressList createFromParcel(Parcel source) {
            return new ExpressList(source);
        }

        @Override
        public ExpressList[] newArray(int size) {
            return new ExpressList[size];
        }
    };
}
