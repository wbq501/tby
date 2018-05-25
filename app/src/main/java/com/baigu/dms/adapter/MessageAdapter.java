package com.baigu.dms.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baigu.dms.common.view.messageview.MessageView;
import com.baigu.dms.common.view.messageview.MessageViewProvider;
import com.hyphenate.chat.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends BaseListAdapter<Message> {

    private MessageViewProvider mMessageViewProvider;

    private boolean mIsHistory;


    public MessageAdapter(Context context) {
        super(context);
        mDataList = new ArrayList<Message>();
        mMessageViewProvider = new MessageViewProvider(mContext);
    }

    public boolean isHistory() {
        return mIsHistory;
    }

    public void setHistory(boolean history) {
        mIsHistory = history;
    }

    public void appendDataToFront(List<Message> list) {
        mDataList.addAll(0, list);
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageViewProvider.getItemViewType(mDataList.get(position));
    }

    @Override
    public int getViewTypeCount() {
        return mMessageViewProvider.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = mDataList.get(position);
        if (convertView == null) {
            convertView = mMessageViewProvider.getItemView(message, position);
        }
        ((MessageView) convertView).setupView(this, message, position);
        return convertView;
    }

}
