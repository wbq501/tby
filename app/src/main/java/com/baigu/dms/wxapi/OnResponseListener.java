package com.baigu.dms.wxapi;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/4 11:06
 */
public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
