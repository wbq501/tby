package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.User;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface UserPresenter extends BasePresenter {
    void getMyInfo(boolean showDialog);
    void loadToken();
    void saveHead(String path,String token);

    interface UserView {
        void onGetMyInfo(User user);
        void onLoadToken(String token);
        void onSaveHead(boolean result);
    }
}
