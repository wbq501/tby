package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/22 21:36
 */
public class Address implements Parcelable {
    private String id;
    private String address;
    private String phone;
    private String regionid;
    private String shipTo;
    private String userid;
    private String name;
    private String areaId;
    private boolean isDefault;
    private String updatetime;
    private String fullRegionName;


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getFullRegionName() {
        return fullRegionName;
    }

    public void setFullRegionName(String fullRegionName) {
        this.fullRegionName = fullRegionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address() {
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Address && id != null) {
            Address other = (Address) o;
            return id.equals(other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.regionid);
        dest.writeString(this.shipTo);
        dest.writeString(this.userid);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeString(this.updatetime);
        dest.writeString(this.fullRegionName);
        dest.writeString(this.name);
        dest.writeString(this.areaId);
    }

    protected Address(Parcel in) {
        this.id = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.regionid = in.readString();
        this.shipTo = in.readString();
        this.userid = in.readString();
        this.isDefault = in.readByte() != 0;
        this.updatetime = in.readString();
        this.fullRegionName = in.readString();
        this.name = in.readString();
        this.areaId=in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", regionid='" + regionid + '\'' +
                ", shipTo='" + shipTo + '\'' +
                ", userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", areaId='" + areaId + '\'' +
                ", isDefault=" + isDefault +
                ", updatetime='" + updatetime + '\'' +
                ", fullRegionName='" + fullRegionName + '\'' +
                '}';
    }
}
