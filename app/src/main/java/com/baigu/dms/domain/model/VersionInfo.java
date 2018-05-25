package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/6 15:02
 */
public class VersionInfo implements Parcelable {

    private int versionCode;
    private String versionName;
    private String downUrl;
    private String updateContent;
    private int versionForced;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getVersionForced() {
        return versionForced;
    }

    public void setVersionForced(int versionForced) {
        this.versionForced = versionForced;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public VersionInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.versionCode);
        dest.writeString(this.versionName);
        dest.writeString(this.downUrl);
        dest.writeString(this.updateContent);
        dest.writeInt(this.versionForced);
    }

    protected VersionInfo(Parcel in) {
        this.versionCode = in.readInt();
        this.versionName = in.readString();
        this.downUrl = in.readString();
        this.updateContent = in.readString();
        this.versionForced = in.readInt();
    }

    public static final Creator<VersionInfo> CREATOR = new Creator<VersionInfo>() {
        @Override
        public VersionInfo createFromParcel(Parcel source) {
            return new VersionInfo(source);
        }

        @Override
        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };
}
