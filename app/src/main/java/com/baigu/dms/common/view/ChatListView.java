package com.baigu.dms.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ChatListView extends ListView {

    private ChatListViewListener mChatListViewListener;

    public ChatListView(Context context) {
        super(context);
        init();
    }

    public ChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setChatListViewListener(ChatListViewListener listener) {
        mChatListViewListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int d = ev.getAction();
        switch (d) {
            case MotionEvent.ACTION_DOWN:
                if (mChatListViewListener != null) {
                    mChatListViewListener.onChatListViewTouchEvent(ev);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface ChatListViewListener {
        void onChatListViewTouchEvent(MotionEvent ev);
    }
}
