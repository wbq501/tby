package com.baigu.dms.event;

import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.hyphenate.chat.Message;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/18 12:02
 */
public class UpdateMessageEvent extends RxBusEvent {

    private Message message;

    public UpdateMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
