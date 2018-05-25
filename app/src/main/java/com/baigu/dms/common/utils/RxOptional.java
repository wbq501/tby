package com.baigu.dms.common.utils;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/05 14:33
 */
public class RxOptional<M> {
    private M optional;
    private int code;

    public RxOptional() {}

    public RxOptional(M m) {
        optional = m;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public void setResult(M optional) {
        this.optional = optional;
    }

    public M get() {
        return optional;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
