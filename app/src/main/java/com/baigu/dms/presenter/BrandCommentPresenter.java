package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface BrandCommentPresenter extends BasePresenter {
    void addComment(String brandId, String comment);

    interface CommentView {
        void onAddComment(String result);
    }
}
