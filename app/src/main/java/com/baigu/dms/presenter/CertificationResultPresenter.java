package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.CertificationResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface CertificationResultPresenter extends BasePresenter {
    void loadCertificationResult(String userId);

    interface CertificationResultView {
        void loadResult(BaseResponse<CertificationResult> result);
    }

}
