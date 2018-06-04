package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WithdrawHistory implements Parcelable{
    
    private String id;
    private String memberId;
    private String bankId;
    private String nickname;
    private String openid;
    private int applyType;
    private int applyStatus;
    private int applyAmount;
    private long applyTime;
    private long confirmTime;
    private long payTime;
    private String payNo;
    private String opUser;
    private String remark;

    private long createDate;
    private long updateDate;

    private int expRecType;
    private int amount;
    private String describetion;
    private String createBy;
    private String updateBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }

    public int getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(int applyAmount) {
        this.applyAmount = applyAmount;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(long confirmTime) {
        this.confirmTime = confirmTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getExpRecType() {
        return expRecType;
    }

    public void setExpRecType(int expRecType) {
        this.expRecType = expRecType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescribetion() {
        return describetion;
    }

    public void setDescribetion(String describetion) {
        this.describetion = describetion;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.memberId);
        dest.writeString(this.bankId);
        dest.writeString(this.nickname);
        dest.writeString(this.openid);
        dest.writeInt(this.applyType);
        dest.writeInt(this.applyStatus);
        dest.writeInt(this.applyAmount);
        dest.writeLong(this.applyTime);
        dest.writeLong(this.confirmTime);
        dest.writeLong(this.payTime);
        dest.writeString(this.payNo);
        dest.writeString(this.opUser);
        dest.writeString(this.remark);
        dest.writeLong(this.createDate);
        dest.writeLong(this.updateDate);
        dest.writeInt(this.expRecType);
        dest.writeInt(this.amount);
        dest.writeString(this.describetion);
        dest.writeString(this.createBy);
        dest.writeString(this.updateBy);
    }

    public WithdrawHistory() {
    }

    protected WithdrawHistory(Parcel in) {
        this.id = in.readString();
        this.memberId = in.readString();
        this.bankId = in.readString();
        this.nickname = in.readString();
        this.openid = in.readString();
        this.applyType = in.readInt();
        this.applyStatus = in.readInt();
        this.applyAmount = in.readInt();
        this.applyTime = in.readLong();
        this.confirmTime = in.readLong();
        this.payTime = in.readLong();
        this.payNo = in.readString();
        this.opUser = in.readString();
        this.remark = in.readString();
        this.createDate = in.readLong();
        this.updateDate = in.readLong();
        this.expRecType = in.readInt();
        this.amount = in.readInt();
        this.describetion = in.readString();
        this.createBy = in.readString();
        this.updateBy = in.readString();
    }

    public static final Creator<WithdrawHistory> CREATOR = new Creator<WithdrawHistory>() {
        @Override
        public WithdrawHistory createFromParcel(Parcel source) {
            return new WithdrawHistory(source);
        }

        @Override
        public WithdrawHistory[] newArray(int size) {
            return new WithdrawHistory[size];
        }
    };
}
