package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description 评论
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/17 11:23
 */
public class Comment implements Parcelable {

    private String ids;
    private String content;
    private String membername;
    private String photo;
    private String create_time;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Comment() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.content);
        dest.writeString(this.membername);
        dest.writeString(this.photo);
        dest.writeString(this.create_time);
    }

    protected Comment(Parcel in) {
        this.ids = in.readString();
        this.content = in.readString();
        this.membername = in.readString();
        this.photo = in.readString();
        this.create_time = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
