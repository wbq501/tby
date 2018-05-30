package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/31 11:58
 */
@Entity
public class BankType implements Parcelable {
    @Id
    private String id;
    private String name;
    private String value;
    private String remarks;
    private int type;
    private int sort;

    @Generated(hash = 26720188)
    public BankType(String id, String name, String value, String remarks, int type,
            int sort) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.remarks = remarks;
        this.type = type;
        this.sort = sort;
    }

    @Generated(hash = 1352759747)
    public BankType() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    protected BankType(Parcel in) {
        id = in.readString();
        name = in.readString();
        value = in.readString();
        remarks = in.readString();
        type = in.readInt();
        sort = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(remarks);
        dest.writeInt(type);
        dest.writeInt(sort);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankType> CREATOR = new Creator<BankType>() {
        @Override
        public BankType createFromParcel(Parcel in) {
            return new BankType(in);
        }

        @Override
        public BankType[] newArray(int size) {
            return new BankType[size];
        }
    };
}
