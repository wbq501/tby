package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class City implements Parcelable {

    @Id
    private String id;
    @Property(nameInDb = "PARENTID")
    private String parentId;
    @Property(nameInDb = "PARENTIDS")
    private String parentIds;
    @Property(nameInDb = "PARENTNAME")
    private String parentName;
    @Property(nameInDb = "PARENTNAMES")
    private String parentNames;
    private int type;
    private String code;
    private String name;
    private int sort;

    @Generated(hash = 1833591998)
    public City(String id, String parentId, String parentIds, String parentName, String parentNames, int type, String code, String name, int sort) {
        this.id = id;
        this.parentId = parentId;
        this.parentIds = parentIds;
        this.parentName = parentName;
        this.parentNames = parentNames;
        this.type = type;
        this.code = code;
        this.name = name;
        this.sort = sort;
    }

    @Generated(hash = 750791287)
    public City() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentNames() {
        return parentNames;
    }

    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(this.parentId);
        dest.writeString(this.parentIds);
        dest.writeString(this.parentName);
        dest.writeString(this.parentNames);
        dest.writeInt(this.type);
        dest.writeInt(this.sort);
        dest.writeString(this.code);
        dest.writeString(this.name);
    }

    protected City(Parcel in) {
        this.id = in.readString();
        this.parentId = in.readString();
        this.parentIds = in.readString();
        this.parentName = in.readString();
        this.parentNames = in.readString();
        this.type = in.readInt();
        this.sort = in.readInt();
        this.code = in.readString();
        this.name = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", prentId='" + parentId + '\'' +
                ", prentIds='" + parentIds + '\'' +
                ", prentName='" + parentName + '\'' +
                ", prentNames='" + parentNames + '\'' +
                ", type=" + type +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
