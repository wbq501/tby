package com.baigu.dms.common.view.imagepicker.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class SDFile extends GalleryImageBean implements Parcelable, Comparable<SDFile> {

    /**文件名称*/
    private String name;

    /**文件类型*/
    private int fileType;

    /**文件大小：**B, **KB, **M, **G, **T*, 或 视频时长HH:ss */
    private String size;

    /**最后修改时间*/
    private String modifyDate;

    /**文件路径*/
    private String path;

    /**图片、视频缩略图*/
    private Uri thumbUri;

    /**父目录*/
    private SDFolder parentFolder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(Uri thumbUri) {
        this.thumbUri = thumbUri;
    }

    public SDFolder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(SDFolder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public SDFile() {
    }

    @Override
    public int compareTo(SDFile another) {
        return name.compareTo(another.getName());
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SDFile && path != null && !"".equals(path)) {
            SDFile file = (SDFile) o;
            return path.equals(file.getPath());
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeInt(this.fileType);
        dest.writeString(this.size);
        dest.writeString(this.modifyDate);
        dest.writeString(this.path);
        dest.writeParcelable(this.thumbUri, flags);
        dest.writeParcelable(this.parentFolder, flags);
    }

    protected SDFile(Parcel in) {
        super(in);
        this.name = in.readString();
        this.fileType = in.readInt();
        this.size = in.readString();
        this.modifyDate = in.readString();
        this.path = in.readString();
        this.thumbUri = in.readParcelable(Uri.class.getClassLoader());
        this.parentFolder = in.readParcelable(SDFolder.class.getClassLoader());
    }

    public static final Creator<SDFile> CREATOR = new Creator<SDFile>() {
        @Override
        public SDFile createFromParcel(Parcel source) {
            return new SDFile(source);
        }

        @Override
        public SDFile[] newArray(int size) {
            return new SDFile[size];
        }
    };
}
