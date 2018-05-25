package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface RegisterPresenter extends BasePresenter {

    void getUserByInviteCode(String inviteCode);
    void register(String phone,
                  String realname,
                  String pwd,
                  String code,
                  String inviteCode,
//                  String banktype,
//                  String banknumber,
//                  String alipayaccount,
                  String wxaccount);

    interface RegisterView {
        void onGetUserByInviteCode(String user);
        void onRegister(boolean b);
    }
}