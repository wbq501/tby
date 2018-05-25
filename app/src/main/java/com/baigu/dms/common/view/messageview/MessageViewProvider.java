package com.baigu.dms.common.view.messageview;

import android.content.Context;

import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.model.MessageHelper;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public class MessageViewProvider {

    public static final int TYPE_RECV_TEXT = 0;
    public static final int TYPE_SEND_TEXT = 1;
    public static final int TYPE_RECV_IMAGE = 2;
    public static final int TYPE_SEND_IMAGE = 3;
    public static final int TYPE_SENT_EVAL = 4;
    public static final int TYPE_RECV_EVAL = 5;

    private Context mContext;

    public MessageViewProvider(Context context) {
        mContext = context;
    }

    public int getViewTypeCount() {
        return 6;
    }

    private boolean isSelfSend(Message message) {
        return message.direct() == Message.Direct.SEND;
    }

    public int getItemViewType(Message message) {
        int type = -1;
        if (MessageHelper.getEvalRequest(message) != null) {
            return isSelfSend(message) ? TYPE_RECV_EVAL : TYPE_SENT_EVAL;
        }
        switch (message.getType()) {
            case TXT:
                type = isSelfSend(message) ? TYPE_RECV_TEXT : TYPE_SEND_TEXT;
                break;
            case IMAGE:
                type = isSelfSend(message) ? TYPE_RECV_IMAGE : TYPE_SEND_IMAGE;
                break;
            default:
                type = isSelfSend(message) ? TYPE_RECV_TEXT : TYPE_SEND_TEXT;
                break;
        }
        return type;
    }

    public MessageView getItemView(Message message, int position) {
        MessageView messageView = null;
        if (MessageHelper.getEvalRequest(message) != null) {
            messageView = new MessageEvaluationView(mContext, message, position);
        } else {
            switch (message.getType()) {
                case TXT:
                    messageView = new MessageTextView(mContext, message, position);
                    break;
                case IMAGE:
                    messageView = new MessageImageView(mContext, message, position);
                    break;
                default:
                    messageView = new MessageTextView(mContext, message, position);
                    break;
            }
        }
        return messageView;
    }

}
