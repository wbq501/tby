package com.baigu.dms.common.view.messageview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.view.emotionskeyboard.hhboard.HHBoardUtils;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.Message;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public class MessageTextView extends MessageView {

    private TextView mTvContent;

    public MessageTextView(Context context, Message message, int position) {
        super(context, message, position);
    }

    @Override
    protected void onInflateView() {
        int layoutId = isSelfSend() ? R.layout.item_message_text_to : R.layout.item_message_text_from;
        mInflater.inflate(layoutId, this);
    }

    @Override
    protected void onFindView() {
        mTvContent = findView(R.id.tv_content);
        mTvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onBubbleLongClick();
                return true;
            }
        });
    }

    @Override
    protected void onSetupView() {
        if (mMessage.getBody() instanceof EMTextMessageBody) {
            EMTextMessageBody body = (EMTextMessageBody) mMessage.getBody();
            HHBoardUtils.styleBuilderEmoticonFilterWithUrl(mTvContent, body.getMessage());
        }
    }

    @Override
    protected void onBubbleClick() {

    }

    @Override
    protected void onBubbleLongClick() {

    }
}
