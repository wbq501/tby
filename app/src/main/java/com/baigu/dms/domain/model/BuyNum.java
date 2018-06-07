package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BuyNum implements Parcelable{

    private int code;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.result);
    }

    public BuyNum() {
    }

    protected BuyNum(Parcel in) {
        this.code = in.readInt();
        this.result = in.readString();
    }

    public static final Creator<BuyNum> CREATOR = new Creator<BuyNum>() {
        @Override
        public BuyNum createFromParcel(Parcel source) {
            return new BuyNum(source);
        }

        @Override
        public BuyNum[] newArray(int size) {
            return new BuyNum[size];
        }
    };
}
