package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface BrandStoryPresenter extends BasePresenter {

    void loadList(String pageNum,boolean isLoading);

    interface BrandStoryView {
        void onLoad(BaseResponse<PageResult<BrandStory>> data);
    }
}
