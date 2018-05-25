package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface BrandQuestionPresenter extends BasePresenter {

    void getBrandQuestion(int pageNum);

    void getBrandQuestionDetail(String id);

    interface BrandQuestionView {
        void onGetBrandQuestion(PageResult<BrandQuestion> pageResult);
        void onGetBrandQuestionDetail(BrandQuestion question);
    }
}
