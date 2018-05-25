package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 品牌故事
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/3 19:22
 */
public class BrandStory implements Parcelable {

    private String brand_title;
    private String brand_content;
    private String brand_ctx_img;
    private String brand_img;
    private String create_time;
    private long dd;
    private String ids;
    private long ss;
    private int isdz;
    private String id;
    private String brand_brief;
    private List<String> urlList;

    protected BrandStory(Parcel in) {
        brand_title = in.readString();
        brand_content = in.readString();
        brand_ctx_img = in.readString();
        brand_img = in.readString();
        create_time = in.readString();
        dd = in.readLong();
        ids = in.readString();
        ss = in.readLong();
        isdz = in.readInt();
        id = in.readString();
        urlList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand_title);
        dest.writeString(brand_content);
        dest.writeString(brand_ctx_img);
        dest.writeString(brand_img);
        dest.writeString(create_time);
        dest.writeLong(dd);
        dest.writeString(ids);
        dest.writeLong(ss);
        dest.writeInt(isdz);
        dest.writeString(id);
        dest.writeStringList(urlList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BrandStory> CREATOR = new Creator<BrandStory>() {
        @Override
        public BrandStory createFromParcel(Parcel in) {
            return new BrandStory(in);
        }

        @Override
        public BrandStory[] newArray(int size) {
            return new BrandStory[size];
        }
    };

    public List<String> getUrlList() {
        if (urlList == null && !TextUtils.isEmpty(brand_ctx_img)) {
            urlList = new ArrayList<>();
            for (String url : brand_ctx_img.split(",")) {
                urlList.add(url);
            }
        }
        return urlList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_title() {
        return brand_title;
    }

    public void setBrand_title(String brand_title) {
        this.brand_title = brand_title;
    }

    public String getBrand_content() {
        return brand_content;
    }

    public void setBrand_content(String brand_content) {
        this.brand_content = brand_content;
    }

    public String getBrand_img() {
        return brand_img;
    }

    public void setBrand_img(String brand_img) {
        this.brand_img = brand_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public long getDd() {
        return dd;
    }

    public void setDd(long dd) {
        this.dd = dd;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public long getSs() {
        return ss;
    }

    public void setSs(long ss) {
        this.ss = ss;
    }

    public String getBrand_ctx_img() {
        return brand_ctx_img;
    }

    public void setBrand_ctx_img(String brand_ctx_img) {
        this.brand_ctx_img = brand_ctx_img;
    }

    public int getIsdz() {
        return isdz;
    }

    public void setIsdz(int isdz) {
        this.isdz = isdz;
    }

    public String getBrand_brief() {
        return brand_brief;
    }

    public void setBrand_brief(String brand_brief) {
        this.brand_brief = brand_brief;
    }

    public BrandStory() {
    }


}
