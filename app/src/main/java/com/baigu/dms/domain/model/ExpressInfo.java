package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ExpressInfo implements Serializable{

    public String time;
    public String ftime;
    public String context;
    public String location;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ExpressInfo{" +
                "time='" + time + '\'' +
                ", ftime='" + ftime + '\'' +
                ", context='" + context + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
