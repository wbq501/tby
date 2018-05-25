package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description 点赞
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/17 11:33
 */
public class Praise implements Parcelable {

    private String memberid;
    private String membername;
    private String photo;
    private String create_time;

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.memberid);
        dest.writeString(this.membername);
        dest.writeString(this.photo);
        dest.writeString(this.create_time);
    }

    public Praise() {
    }

    protected Praise(Parcel in) {
        this.memberid = in.readString();
        this.membername = in.readString();
        this.photo = in.readString();
        this.create_time = in.readString();
    }

    public static final Parcelable.Creator<Praise> CREATOR = new Parcelable.Creator<Praise>() {
        @Override
        public Praise createFromParcel(Parcel source) {
            return new Praise(source);
        }

        @Override
        public Praise[] newArray(int size) {
            return new Praise[size];
        }
    };
}

