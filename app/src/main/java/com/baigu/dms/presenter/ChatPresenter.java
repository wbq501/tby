package com.baigu.dms.presenter;

import com.hyphenate.chat.Message;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface ChatPresenter extends BasePresenter {

    void sendTextMessage(String to, String text);

    void sendImageMessage(String to, String imagePath, boolean sendOriginalImage);

    void loadMessageList(final String to, final String startMsgId);

    interface ChatView {
        void onLoadMessageList(List<Message> list);
    }
}
