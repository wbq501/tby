package com.baigu.dms.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:21
 */
public class User implements Serializable, Parcelable {
    private String address;
    private String blance;
    private String career;
    private String cellphone;
    private String createdate;
    private int disabled;
    private String email;
    private String expenditure;
    private String feteday;
    private String ids;
    private String inviteuserid;
    private String lastlogindate;
    private String myqrcode;
    private String myqrcodepress;
    private String nick;
    private String ordernumber;
    private int parentsellerid;
    private String password;
    private String passwordsalt;
    private String paypwd;
    private String paypwdsalt;
    private String perentid;
    private String photo;
    private String pids;
    private String points;
    private String qq;
    private String realname;
    private String regionid;
    private String remark;
    private String score;
    private String sex;
    private String shareuserid;
    private String topregionid;
    private String username;
    private String wx_account;
    private String hxAccount;
    private String hxPasswd;
    private String idcard;
    private String idcardimg;
    private String idcardstatus;
    private String invitecode;
    private String realName;
    private String imuser;
    private String impwd;
    private String privateKey;
    private String loginToken;
    private String token;
    private String phone;
    private String id;

    ///微信
    private String wxaccount;
    /// 支付宝账号
    private String alipayaccount;
    /// 银行开户行
    private String blankname;
    /// 银行账号
    private String blanknumber;

    protected User(Parcel in) {
        address = in.readString();
        blance = in.readString();
        career = in.readString();
        cellphone = in.readString();
        createdate = in.readString();
        disabled = in.readInt();
        email = in.readString();
        expenditure = in.readString();
        feteday = in.readString();
        ids = in.readString();
        inviteuserid = in.readString();
        lastlogindate = in.readString();
        myqrcode = in.readString();
        myqrcodepress = in.readString();
        nick = in.readString();
        ordernumber = in.readString();
        parentsellerid = in.readInt();
        password = in.readString();
        passwordsalt = in.readString();
        paypwd = in.readString();
        paypwdsalt = in.readString();
        perentid = in.readString();
        photo = in.readString();
        pids = in.readString();
        points = in.readString();
        qq = in.readString();
        realname = in.readString();
        regionid = in.readString();
        remark = in.readString();
        score = in.readString();
        sex = in.readString();
        shareuserid = in.readString();
        topregionid = in.readString();
        username = in.readString();
        wx_account = in.readString();
        hxAccount = in.readString();
        hxPasswd = in.readString();
        idcard = in.readString();
        idcardimg = in.readString();
        idcardstatus = in.readString();
        invitecode = in.readString();
        realName = in.readString();
        imuser = in.readString();
        impwd = in.readString();
        privateKey = in.readString();
        loginToken = in.readString();
        token = in.readString();
        phone = in.readString();
        id = in.readString();
        wxaccount = in.readString();
        alipayaccount = in.readString();
        blankname = in.readString();
        blanknumber = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(blance);
        parcel.writeString(career);
        parcel.writeString(cellphone);
        parcel.writeString(createdate);
        parcel.writeInt(disabled);
        parcel.writeString(email);
        parcel.writeString(expenditure);
        parcel.writeString(feteday);
        parcel.writeString(ids);
        parcel.writeString(inviteuserid);
        parcel.writeString(lastlogindate);
        parcel.writeString(myqrcode);
        parcel.writeString(myqrcodepress);
        parcel.writeString(nick);
        parcel.writeString(ordernumber);
        parcel.writeInt(parentsellerid);
        parcel.writeString(password);
        parcel.writeString(passwordsalt);
        parcel.writeString(paypwd);
        parcel.writeString(paypwdsalt);
        parcel.writeString(perentid);
        parcel.writeString(photo);
        parcel.writeString(pids);
        parcel.writeString(points);
        parcel.writeString(qq);
        parcel.writeString(realname);
        parcel.writeString(regionid);
        parcel.writeString(remark);
        parcel.writeString(score);
        parcel.writeString(sex);
        parcel.writeString(shareuserid);
        parcel.writeString(topregionid);
        parcel.writeString(username);
        parcel.writeString(wx_account);
        parcel.writeString(hxAccount);
        parcel.writeString(hxPasswd);
        parcel.writeString(idcard);
        parcel.writeString(idcardimg);
        parcel.writeString(idcardstatus);
        parcel.writeString(invitecode);
        parcel.writeString(realName);
        parcel.writeString(imuser);
        parcel.writeString(impwd);
        parcel.writeString(privateKey);
        parcel.writeString(loginToken);
        parcel.writeString(token);
        parcel.writeString(phone);
        parcel.writeString(id);
        parcel.writeString(wxaccount);
        parcel.writeString(alipayaccount);
        parcel.writeString(blankname);
        parcel.writeString(blanknumber);
    }


    public static class IDCardStatus {
        public static final String VERIFY_SUCCESS = "1";
        public static final String VERIFY_DOING = "0";
        public static final String VERIFY_FAILED = "2";
        public static final String VERIFY_NONE = "4";
    }

    public static class SMSCodeType {
        /**注册*/
        public static final String REGISTER = "1";
        /**更新登录密码*/
        public static final String UPD_LOGIN_PWD = "2";
        /**更新支付密码*/
        public static final String UPD_PAY_PWD = "3";
        /**修改手机号-原手机号*/
        public static final String OLD_PHONE = "4";
        /**修改手机号-新手机号*/
        public static final String NEW_PHONE = "5";
    }

    public static class RSAKeyType {
        /**登录*/
        public static final String LOGIN = "1";
        /**注册*/
        public static final String REGISTER = "2";
        /**修改密码*/
        public static final String UPD_PWD = "3";
        /**忘记密码*/
        public static final String REFIND_PWD = "4";
        /**修改支付密码*/
        public static final String UPD_PAY_PWD = "5";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlance() {
        return blance;
    }

    public void setBlance(String blance) {
        this.blance = blance;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getWxaccount() {
        return wxaccount;
    }

    public void setWxaccount(String wxaccount) {
        this.wxaccount = wxaccount;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getFeteday() {
        return feteday;
    }

    public void setFeteday(String feteday) {
        this.feteday = feteday;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInviteuserid() {
        return inviteuserid;
    }

    public void setInviteuserid(String inviteuserid) {
        this.inviteuserid = inviteuserid;
    }

    public String getLastlogindate() {
        return lastlogindate;
    }

    public void setLastlogindate(String lastlogindate) {
        this.lastlogindate = lastlogindate;
    }

    public String getMyqrcode() {
        return myqrcode;
    }

    public void setMyqrcode(String myqrcode) {
        this.myqrcode = myqrcode;
    }

    public String getMyqrcodepress() {
        return myqrcodepress;
    }

    public void setMyqrcodepress(String myqrcodepress) {
        this.myqrcodepress = myqrcodepress;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getParentsellerid() {
        return parentsellerid;
    }

    public void setParentsellerid(int parentsellerid) {
        this.parentsellerid = parentsellerid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordsalt() {
        return passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getPaypwdsalt() {
        return paypwdsalt;
    }

    public void setPaypwdsalt(String paypwdsalt) {
        this.paypwdsalt = paypwdsalt;
    }

    public String getPerentid() {
        return perentid;
    }

    public void setPerentid(String perentid) {
        this.perentid = perentid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getShareuserid() {
        return shareuserid;
    }

    public void setShareuserid(String shareuserid) {
        this.shareuserid = shareuserid;
    }

    public String getTopregionid() {
        return topregionid;
    }

    public void setTopregionid(String topregionid) {
        this.topregionid = topregionid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWx_account() {
        return wx_account;
    }

    public void setWx_account(String wx_account) {
        this.wx_account = wx_account;
    }

    public String getHxAccount() {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount) {
        this.hxAccount = hxAccount;
    }

    public String getHxPasswd() {
        return hxPasswd;
    }

    public void setHxPasswd(String hxPasswd) {
        this.hxPasswd = hxPasswd;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAlipayaccount() {
        return alipayaccount;
    }

    public void setAlipayaccount(String alipayaccount) {
        this.alipayaccount = alipayaccount;
    }

    public String getBlankname() {
        return blankname;
    }

    public void setBlankname(String blankname) {
        this.blankname = blankname;
    }

    public String getBlanknumber() {
        return blanknumber;
    }

    public void setBlanknumber(String blanknumber) {
        this.blanknumber = blanknumber;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardimg() {
        return idcardimg;
    }

    public void setIdcardimg(String idcardimg) {
        this.idcardimg = idcardimg;
    }

    public String getIdcardstatus() {
        return idcardstatus;
    }

    public void setIdcardstatus(String idcardstatus) {
        this.idcardstatus = idcardstatus;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getImuser() {
        return imuser;
    }

    public void setImuser(String imuser) {
        this.imuser = imuser;
    }

    public String getImpwd() {
        return impwd;
    }

    public void setImpwd(String impwd) {
        this.impwd = impwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }



    @Override
    public String toString() {
        return "User{" +
                "address='" + address + '\'' +
                ", blance='" + blance + '\'' +
                ", career='" + career + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", createdate='" + createdate + '\'' +
                ", disabled=" + disabled +
                ", email='" + email + '\'' +
                ", expenditure='" + expenditure + '\'' +
                ", feteday='" + feteday + '\'' +
                ", ids='" + ids + '\'' +
                ", inviteuserid='" + inviteuserid + '\'' +
                ", lastlogindate='" + lastlogindate + '\'' +
                ", myqrcode='" + myqrcode + '\'' +
                ", myqrcodepress='" + myqrcodepress + '\'' +
                ", nick='" + nick + '\'' +
                ", ordernumber='" + ordernumber + '\'' +
                ", parentsellerid=" + parentsellerid +
                ", password='" + password + '\'' +
                ", passwordsalt='" + passwordsalt + '\'' +
                ", paypwd='" + paypwd + '\'' +
                ", paypwdsalt='" + paypwdsalt + '\'' +
                ", perentid='" + perentid + '\'' +
                ", photo='" + photo + '\'' +
                ", pids='" + pids + '\'' +
                ", points='" + points + '\'' +
                ", qq='" + qq + '\'' +
                ", realname='" + realname + '\'' +
                ", regionid='" + regionid + '\'' +
                ", remark='" + remark + '\'' +
                ", score='" + score + '\'' +
                ", sex='" + sex + '\'' +
                ", shareuserid='" + shareuserid + '\'' +
                ", topregionid='" + topregionid + '\'' +
                ", username='" + username + '\'' +
                ", wx_account='" + wx_account + '\'' +
                ", hxAccount='" + hxAccount + '\'' +
                ", hxPasswd='" + hxPasswd + '\'' +
                ", idcard='" + idcard + '\'' +
                ", idcardimg='" + idcardimg + '\'' +
                ", idcardstatus='" + idcardstatus + '\'' +
                ", invitecode='" + invitecode + '\'' +
                ", realName='" + realName + '\'' +
                ", imuser='" + imuser + '\'' +
                ", impwd='" + impwd + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", loginToken='" + loginToken + '\'' +
                ", token='" + token + '\'' +
                ", wxaccount='" + wxaccount + '\'' +
                ", alipayaccount='" + alipayaccount + '\'' +
                ", blankname='" + blankname + '\'' +
                ", blanknumber='" + blanknumber + '\'' +
                '}';
    }


}
