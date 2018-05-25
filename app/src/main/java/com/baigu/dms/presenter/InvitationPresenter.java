package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.common.model.PageResult;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface InvitationPresenter extends BasePresenter {
    void loadInvitationVerified(int pageNum, boolean showDialog);
    void loadInvitationUnVerify(int pageNum, boolean showDialog);
    void verify(String requestUserId);

    interface InvitaionView {
        void onLoadInvitationVerified(PageResult<User> result);
        void onLoadInvitationUnVerify(PageResult<User> result);
        void onVerify(boolean result);
    }
}
