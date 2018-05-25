package com.baigu.dms.event;

import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.hyphenate.chat.Message;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/18 11:55
 */
public class NewMessageEvent extends RxBusEvent {
    private List<Message> messageList;

    public NewMessageEvent(List<Message> list) {
        this.messageList = list;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
