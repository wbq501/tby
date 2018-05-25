package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 23:13
 */
public interface QRCodePresenter extends BasePresenter  {
    void decode(String path);

    interface QRCodeView {
        void onDecode(String result);
    }

}
