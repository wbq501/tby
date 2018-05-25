package com.baigu.dms.domain.model;

import java.io.Serializable;

public class ExpressType implements Serializable{

    private String comCode;
    private String id;
    private int noCount;
    private String noPre;
    private String startTime;

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNoCount() {
        return noCount;
    }

    public void setNoCount(int noCount) {
        this.noCount = noCount;
    }

    public String getNoPre() {
        return noPre;
    }

    public void setNoPre(String noPre) {
        this.noPre = noPre;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
