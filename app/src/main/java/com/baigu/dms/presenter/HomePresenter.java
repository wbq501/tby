package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.common.model.HomeResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface HomePresenter extends BasePresenter {

    void loadHomeData(boolean showProgress);
    void loadBrandStory(int pageNum);

    interface HomeView {
        void onLoadHomeData(HomeResult homeData);
        void onLoadBrandStory(BaseResponse<List<BrandStory>> brandStoryResult);
    }
}
