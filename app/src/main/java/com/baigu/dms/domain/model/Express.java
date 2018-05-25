package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Express implements Parcelable {

    @Id
    private String id;

    @Property
    private String name;

    @Property
    private int type;

    @Property
    private String value;

    @Property
    private int sort;

    @Generated(hash = 1430006141)
    public Express(String id, String name, int type, String value, int sort) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.sort = sort;
    }

    @Generated(hash = 760607181)
    public Express() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeInt(this.type);
        dest.writeString(this.value);
        dest.writeInt(this.sort);
    }


    protected Express(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.type = in.readInt();
        this.value = in.readString();
        this.sort = in.readInt();
    }

    public static final Creator<Express> CREATOR = new Creator<Express>() {
        @Override
        public Express createFromParcel(Parcel source) {
            return new Express(source);
        }

        @Override
        public Express[] newArray(int size) {
            return new Express[size];
        }
    };
}
