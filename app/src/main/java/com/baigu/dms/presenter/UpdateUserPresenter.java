package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface UpdateUserPresenter extends BasePresenter {
    void updateNick(String name);
    void updateEmail(String email);
    void updateWeixin(String weixin);
    void updatePasswd(String oldPasswd, String newPasswd);
    void updatePayPasswd(String oldpwd, String phone, String passwd, String code);


    interface UpdateUserView {
        void onUpdateUser(boolean result);
    }
}
