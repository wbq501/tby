package com.baigu.dms.domain.model;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class CertificationResult {
    private String idCardStatus;
    private String failReason;

    public String getIdCardStatus() {
        return idCardStatus;
    }

    public void setIdCardStatus(String idCardStatus) {
        this.idCardStatus = idCardStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
