package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:28
 */
public class Notice implements Parcelable {
    private String ids;
    private String btitle;
    private String bcontent;
    private String content;

    protected Notice(Parcel in) {
        ids = in.readString();
        btitle = in.readString();
        bcontent = in.readString();
        content = in.readString();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getBcontent() {
        return bcontent;
    }

    public void setBcontent(String bcontent) {
        this.bcontent = bcontent;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Notice() {
    }



    @Override
    public String toString() {
        return "Notice{" +
                "ids='" + ids + '\'' +
                ", btitle='" + btitle + '\'' +
                ", bcontent='" + bcontent + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ids);
        parcel.writeString(btitle);
        parcel.writeString(bcontent);
        parcel.writeString(content);
    }
}
