package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Notice;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface NoticePresenter extends BasePresenter {

    void getNoticeList(String pageNum);
    void getNotice(String ids);

    interface NoticeView {
        void onGetNoticeList(List<Notice> list);
        void onGetNotice(Notice notice);
    }
}