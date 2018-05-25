package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface CertificationPresenter extends BasePresenter {
    void saveIDCard(String idCard, String idcardFrontPath, String idcardBackPath,String payPwd);

    void upLoadImage(String imageFile,int type);

    interface CertificationView {
        void onSaveIDCard(boolean b);
        void onLoadImage(String imgURL);
        void onLoadImageBack(String imgURL);
    }
}
