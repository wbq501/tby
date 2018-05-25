package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Agreement;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.domain.netservice.response.BaseResponse;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface CheckVersionPresenter extends BasePresenter {
    void checkVersion(boolean showDialog);
    void loadUrl();

    interface CheckVersionView {
        void onCheckVersion(VersionInfo versionInfo);
        void onLoad(BaseResponse<Agreement> agreement);
    }
}
