package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 银行
 * Created by Administrator on 2017/11/22.
 */

public class Bank implements Parcelable{

    private String id;
    private String name;
    private String bankAccount;
    private String icon;
    private String bankCode;
    private String bankOpenName;
    private String bankName;
    private String remarks;



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


    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankOpenName() {
        return bankOpenName;
    }

    public void setBankOpenName(String bankOpenName) {
        this.bankOpenName = bankOpenName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Bank(String id, String name, String bankAccount, String icon, String bankCode, String bankOpenName, String bankName,String remarks) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.icon = icon;
        this.bankCode = bankCode;
        this.bankOpenName = bankOpenName;
        this.bankName = bankName;
        this.remarks = remarks;
    }

    public Bank() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(bankAccount);
        parcel.writeString(icon);
        parcel.writeString(bankCode);
        parcel.writeString(bankOpenName);
        parcel.writeString(bankName);
        parcel.writeString(remarks);
    }

    protected Bank(Parcel in) {
        id = in.readString();
        name = in.readString();
        bankAccount = in.readString();
        icon = in.readString();
        bankCode = in.readString();
        bankOpenName = in.readString();
        bankName = in.readString();
        remarks = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };
}
