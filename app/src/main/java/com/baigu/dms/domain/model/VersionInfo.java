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

    private String id;
    private String porttype;
    private String url;
    private String versioncode;
    private String versionname;
    private String applicationdescription;
    private int versionforced;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPorttype() {
        return porttype;
    }

    public void setPorttype(String porttype) {
        this.porttype = porttype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getApplicationdescription() {
        return applicationdescription;
    }

    public void setApplicationdescription(String applicationdescription) {
        this.applicationdescription = applicationdescription;
    }

    public int getVersionforced() {
        return versionforced;
    }

    public void setVersionforced(int versionforced) {
        this.versionforced = versionforced;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.porttype);
        dest.writeString(this.url);
        dest.writeString(this.versioncode);
        dest.writeString(this.versionname);
        dest.writeString(this.applicationdescription);
        dest.writeInt(this.versionforced);
    }

    public VersionInfo() {
    }

    protected VersionInfo(Parcel in) {
        this.id = in.readString();
        this.porttype = in.readString();
        this.url = in.readString();
        this.versioncode = in.readString();
        this.versionname = in.readString();
        this.applicationdescription = in.readString();
        this.versionforced = in.readInt();
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
