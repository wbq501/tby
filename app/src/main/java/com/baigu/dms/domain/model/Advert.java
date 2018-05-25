package com.baigu.dms.domain.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Advert implements Parcelable {
    private String advertis_img;
    private String advertis_title;
    private String advertis_content;

    public String getAdvertis_img() {
        return advertis_img;
    }

    public void setAdvertis_img(String advertis_img) {
        this.advertis_img = advertis_img;
    }

    public String getAdvertis_title() {
        return advertis_title;
    }

    public void setAdvertis_title(String advertis_title) {
        this.advertis_title = advertis_title;
    }

    public String getAdvertis_content() {
        return advertis_content;
    }

    @Override
    public String toString() {
        return "Advert{" +
                "advertis_img='" + advertis_img + '\'' +
                ", advertis_title='" + advertis_title + '\'' +
                ", advertis_content='" + advertis_content + '\'' +
                '}';
    }

    public void setAdvertis_content(String advertis_content) {
        this.advertis_content = advertis_content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advertis_img);
        dest.writeString(this.advertis_title);
        dest.writeString(this.advertis_content);
    }

    public Advert() {
    }

    protected Advert(Parcel in) {
        this.advertis_img = in.readString();
        this.advertis_title = in.readString();
        this.advertis_content = in.readString();
    }

    public static final Creator<Advert> CREATOR = new Creator<Advert>() {
        @Override
        public Advert createFromParcel(Parcel source) {
            return new Advert(source);
        }

        @Override
        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };


}
