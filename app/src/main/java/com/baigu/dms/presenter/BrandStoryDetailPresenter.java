package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.domain.model.Praise;
import com.baigu.dms.domain.netservice.common.model.PageResult;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/17 12:02
 */
public interface BrandStoryDetailPresenter extends BasePresenter {

    void loadBrandStoryById(String brandId);

    void loadCommentList(String brandId, int pageNum, boolean isShowDialog);

    void loadPraiseList(String brandId, int pageNum, boolean isShowDialog);

    interface BrandStoryDetailView {

        void onLoadBrandStory(BrandStory brandStory);

        void onLoadCommentList(PageResult<Comment> pageResult);

        void onLoadPraiseList(PageResult<Praise> pageResult);
    }
}
