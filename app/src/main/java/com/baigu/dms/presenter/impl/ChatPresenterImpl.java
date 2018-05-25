package com.baigu.dms.presenter.impl;

import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.IMHelper;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.presenter.ChatPresenter;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.chat.Message;
import com.micky.logger.Logger;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class ChatPresenterImpl extends BasePresenterImpl implements ChatPresenter {

    private ChatView mChatView;

    public ChatPresenterImpl(BaseActivity activity, ChatView chatView) {
        super(activity);
        mChatView = chatView;
    }

    @Override
    public void sendTextMessage(String to, String text) {
        Message message = Message.createTxtSendMessage(text, to);
        IMHelper.getInstance().sendMessage(message);
    }

    @Override
    public void sendImageMessage(String to, String imagePath, boolean sendOriginalImage) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        File imageFile = new File(imagePath);
        if (imageFile == null || !imageFile.exists()) {
            return;
        }

        Message message = Message.createImageSendMessage(imagePath, sendOriginalImage, to);
        IMHelper.getInstance().sendMessage(message);
    }

    @Override
    public void loadMessageList(final String to, final String startMsgId) {
        addDisposable(new BaseAsyncTask<String, Void, List<Message>>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.logining);
            }

            @Override
            protected RxOptional<List<Message>> doInBackground(String... params) {
                RxOptional<List<Message>> rxResult = new RxOptional<>();
                List<Message> messages = null;
                try {
                    Conversation conversation = ChatClient.getInstance().getChat().getConversation(to);
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    messages = conversation.loadMoreMsgFromDB(startMsgId, 20);
                    if (messages != null && messages.size() > 0) {
                        Collections.sort(messages, new Comparator<Message>() {
                            @Override
                            public int compare(Message lhs, Message rhs) {
                                return (int) (lhs.getMsgTime() - rhs.getMsgTime());
                            }
                        });
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(messages);
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<Message> list) {
                super.onPostExecute(list);
                if (mChatView != null) {
                    mChatView.onLoadMessageList(list);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
            }
        }.execute(to));

    }
}
