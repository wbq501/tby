package com.baigu.dms.common.view.imagepicker.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class SDFolder implements Parcelable, Comparable<SDFolder> {

    /**目录名称*/
    private String name = "";

    /**目录路径*/
    private String path;

    /**目录下文件数*/
    private int count;

    /**目录类型*/
    private int folderType;

    /**
     * 图片库、视频库的封面路径
     * */
    private Uri imageUri;

    /**目录下的所有文件*/
    private List<SDFile> fileList;

    /**父目录*/
    private SDFolder parentFolder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        int lastIndexOf = this.path.lastIndexOf("/");
        this.name = this.path.substring(lastIndexOf + 1);
    }

    public int getFolderType() {
        return folderType;
    }

    public void setFolderType(int folderType) {
        this.folderType = folderType;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SDFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<SDFile> fileList) {
        this.fileList = fileList;
    }

    public SDFolder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(SDFolder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public SDFolder() {
    }

    @Override
    public int compareTo(SDFolder another) {
        return name.toUpperCase().compareTo(another.getName().toUpperCase());
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SDFolder && path != null && !"".equals(path)) {
            SDFolder folder = (SDFolder) o;
            return path.equals(folder.getPath());
        }
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeInt(this.count);
        dest.writeInt(this.folderType);
        dest.writeParcelable(this.imageUri, flags);
        dest.writeTypedList(this.fileList);
        dest.writeParcelable(this.parentFolder, flags);
    }

    protected SDFolder(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.count = in.readInt();
        this.folderType = in.readInt();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
        this.fileList = in.createTypedArrayList(SDFile.CREATOR);
        this.parentFolder = in.readParcelable(SDFolder.class.getClassLoader());
    }

    public static final Creator<SDFolder> CREATOR = new Creator<SDFolder>() {
        @Override
        public SDFolder createFromParcel(Parcel source) {
            return new SDFolder(source);
        }

        @Override
        public SDFolder[] newArray(int size) {
            return new SDFolder[size];
        }
    };
}
