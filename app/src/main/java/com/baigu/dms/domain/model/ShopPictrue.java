package com.baigu.dms.domain.model;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class ShopPictrue {

    private  String picId;
    private int position;
    private String picUrl;
    private int sort;


    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
