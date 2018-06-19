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
    private long createTime;

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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ids);
        dest.writeString(this.btitle);
        dest.writeString(this.bcontent);
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
    }

    protected Notice(Parcel in) {
        this.ids = in.readString();
        this.btitle = in.readString();
        this.bcontent = in.readString();
        this.content = in.readString();
        this.createTime = in.readLong();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel source) {
            return new Notice(source);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
