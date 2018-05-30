package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ExclusiveGroups implements Parcelable {
    @Id
    private String id;
    private String value;
    private String name;
    private String type;
    private int sort;

    @Generated(hash = 1330398244)
    public ExclusiveGroups(String id, String value, String name, String type, int sort) {
        this.id = id;
        this.value = value;
        this.name = name;
        this.type = type;
        this.sort = sort;
    }

    @Generated(hash = 711336861)
    public ExclusiveGroups() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeInt(this.sort);
    }

    protected ExclusiveGroups(Parcel in) {
        this.id = in.readString();
        this.value = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.sort = in.readInt();
    }

    public static final Creator<ExclusiveGroups> CREATOR = new Creator<ExclusiveGroups>() {
        @Override
        public ExclusiveGroups createFromParcel(Parcel source) {
            return new ExclusiveGroups(source);
        }

        @Override
        public ExclusiveGroups[] newArray(int size) {
            return new ExclusiveGroups[size];
        }
    };
}
