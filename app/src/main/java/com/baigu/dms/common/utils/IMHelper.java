package com.baigu.dms.common.utils;

import android.content.Context;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.broadcast.IMNotificationReceiver;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.ChatManager;
import com.hyphenate.chat.Conversation;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.VisitorInfo;
import com.micky.logger.Logger;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/18 10:52
 */
public class IMHelper {

    private VisitorInfo mVisitorInfo;

    private ChatManager.MessageListener mMessageListener = null;

    private static class MessageHelperHolder {
        private static IMHelper sInstance = new IMHelper();
    }

    private IMHelper() {
    }

    public static final IMHelper getInstance() {
        return IMHelper.MessageHelperHolder.sInstance;
    }

    public void addMessageListener() {
        removeMessageListener();
        if (mMessageListener == null) {
            mMessageListener = new ChatManager.MessageListener() {
                @Override
                public void onMessage(List<Message> list) {
                    if (UserCache.getInstance().isChatting()) {
                        readMessage(list);
                    } else {
                        if (list != null && list.size() > 0) {
                            Message message = list.get(list.size() - 1);
                            sendNotification(message);
                        }
                    }
                    RxBus.getDefault().post(EventType.TYPE_NEW_MESSAGES, list);
                    RxBus.getDefault().post(EventType.TYPE_UPDATE_MESSAGE_NUM);
                }

                @Override
                public void onCmdMessage(List<Message> list) {
                    if (UserCache.getInstance().isChatting()) {
                        readMessage(list);
                    } else {
                        if (list != null && list.size() > 0) {
                            Message message = list.get(list.size() - 1);
                            sendNotification(message);
                        }
                    }
                    RxBus.getDefault().post(EventType.TYPE_NEW_MESSAGES, list);
                    RxBus.getDefault().post(EventType.TYPE_UPDATE_MESSAGE_NUM);
                }

                @Override
                public void onMessageStatusUpdate() {


                }

                @Override
                public void onMessageSent() {

                }
            };
        }
        ChatClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    private void sendNotification(Message message) {
        Context context = BaseApplication.getContext();
        String content = CommonUtils.getMessageDigest(message, context);
        NotificationUtils.getInstance().sendNotification(context, content, true);
    }

    public void removeMessageListener() {
        if (mMessageListener != null) {
            ChatClient.getInstance().chatManager().removeMessageListener(mMessageListener);
        }
    }

    private void readMessage(List<Message> messageList) {
        if (messageList == null || messageList.size() <= 0) {
            return;
        }
        Conversation conversation = ChatClient.getInstance().chatManager().getConversation(BaseApplication.getContext().getString(R.string.hx_customer));
        for (Message message : messageList) {
            conversation.markMessageAsRead(message.messageId());
        }
    }

    public void logout() {
        try {
            ChatClient.getInstance().logout(true, null);
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        mVisitorInfo = null;
        removeMessageListener();
    }


    public void sendMessage(final Message message) {
        if (mVisitorInfo == null) {
            mVisitorInfo = createVisitorInfo();
        }
        message.addContent(mVisitorInfo);
        RxBus.getDefault().post(EventType.TYPE_NEW_MESSAGE, message);

        ChatClient.getInstance().chatManager().sendMessage(message, new Callback() {
            @Override
            public void onSuccess() {
                RxBus.getDefault().post(EventType.TYPE_MESSAGE_STATUS_UPDATE, message);
            }

            @Override
            public void onError(int i, String s) {
                RxBus.getDefault().post(EventType.TYPE_MESSAGE_STATUS_UPDATE, message);
            }

            @Override
            public void onProgress(int i, String s) {
                RxBus.getDefault().post(EventType.TYPE_MESSAGE_STATUS_UPDATE, message);
            }
        });
    }


    public VisitorInfo createVisitorInfo() {
        VisitorInfo info = ContentFactory.createVisitorInfo(null);
        User user = UserCache.getInstance().getUser();
        info.nickName(user.getNick())
                .name(user.getRealname())
                .qq(user.getQq())
                .phone(user.getCellphone())
                .email(user.getEmail());
        return info;
    }

    public void clearCache() {
        ChatClient.getInstance().chatManager().deleteConversation(BaseApplication.getContext().getString(R.string.hx_customer), true);
    }

}
