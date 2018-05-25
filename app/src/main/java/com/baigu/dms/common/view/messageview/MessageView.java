package com.baigu.dms.common.view.messageview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.MessageAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.URLFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.UserUtil;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

import java.util.Date;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public abstract class MessageView extends LinearLayout implements View.OnClickListener, View
        .OnLongClickListener {

    public static final int DISTANCE_MINUTES_SHOW = 1;

    protected MessageAdapter mMessageAdapter;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected Message mMessage;
    protected int mPosition;

    private ImageView mIvHead;
    protected TextView mTvTime;
//    private int mTimeMargin;
    protected View mLayoutBubble;
    // 用于群组聊天界面，显示发送人姓名
    protected TextView mSendPersonNameText;

    protected ProgressBar mProgressBar;
    protected ImageView mSendErrorImage;
    private Callback mMessageSendCallback;

    /**
     * 长按显示的复制、转发、重发等弹出菜单
     */
//    protected MessagePopMenu mMessagePopMenu;
    public MessageView(Context context, Message message, int position) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mMessage = message;
        mPosition = position;
        initView();
    }

    protected boolean isSelfSend() {
        return mMessage.direct() == Message.Direct.SEND;
    }


    private void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
//        mTimeMargin = (int) getResources().getDimension(R.dimen.message_list_divider_height);

        onInflateView();

        mLayoutBubble = findViewById(R.id.layout_bubble);
        mLayoutBubble.setOnLongClickListener(this);
        mLayoutBubble.setOnClickListener(this);

        mIvHead = findView(R.id.iv_head);
        if (mIvHead != null) {
            mIvHead.setOnClickListener(this);
        }

        //时间 TextView
        mTvTime = new TextView(getContext());
        mTvTime.setIncludeFontPadding(false);
        mTvTime.setTextSize(12);
        mTvTime.setPadding(0, ViewUtils.dip2px(8), 0, ViewUtils.dip2px(8));
        mTvTime.setTextColor(getResources().getColor(R.color.message_time));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
//        params.bottomMargin = mTimeMargin;
        addView(mTvTime, 0, params);

        if (isSelfSend()) {
            mProgressBar = findView(R.id.pb_loading);
            mSendErrorImage = findView(R.id.iv_msg_error);
            if (mSendErrorImage != null) {
                mSendErrorImage.setOnClickListener(this);
            }
        } else {
            mSendPersonNameText = findView(R.id.tv_send_person_name);
        }

        onFindView();
    }

    public void setupView(MessageAdapter adapter, Message message, int position) {
        mMessageAdapter = adapter;
        mMessage = message;
        mPosition = position;
        setupBaseView();
        onSetupView();
    }

    private void setupBaseView() {
        if (!isSelfSend()) {
            setAgentNickAndAvatar(mContext, mMessage, mIvHead, mSendPersonNameText);
        } else {
            User user = UserCache.getInstance().getUser();
            Glide.with(getContext()).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getContext())).into(mIvHead);
        }
        setTime();
        setSendStatus();
    }

    private void setTime() {
        //消息时间显示
        LayoutParams params = (LayoutParams) mTvTime.getLayoutParams();
        if (mTvTime != null) {
            if (mPosition == 0) {
                mTvTime.setText(com.hyphenate.util.DateUtils.getTimestampString(new Date(mMessage.getMsgTime())));
                mTvTime.setVisibility(View.VISIBLE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                Message prevMessage = mMessageAdapter.getItem(mPosition - 1);
                if (prevMessage != null && com.hyphenate.util.DateUtils.isCloseEnough(mMessage.getMsgTime(), prevMessage.getMsgTime())) {
                    mTvTime.setVisibility(View.GONE);
                } else {
                    mTvTime.setText(com.hyphenate.util.DateUtils.getTimestampString(new Date(mMessage.getMsgTime())));
                    mTvTime.setVisibility(View.VISIBLE);
                }
            }
            mTvTime.setLayoutParams(params);
        }
    }

    protected void setSendStatus() {
        if (mMessage.direct() == Message.Direct.SEND) {
            setMessageSendCallback();
            mSendErrorImage.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            switch (mMessage.getStatus()) {
                case CREATE:
                    mProgressBar.setVisibility(View.GONE);
                    // 发送消息
                    break;
                case SUCCESS: // 发送成功
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    mProgressBar.setVisibility(View.GONE);
                    mSendErrorImage.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                     mProgressBar.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback() {
        if (mMessageSendCallback == null) {
            mMessageSendCallback = new Callback() {
                @Override
                public void onSuccess() {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mSendErrorImage.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mSendErrorImage.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onProgress(final int progress, String status) {

                }
            };
        }
        mMessage.setMessageStatusCallback(mMessageSendCallback);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_bubble) {
            onBubbleClick();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.layout_bubble) {
            onBubbleLongClick();
        }
        return true;
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * inflate view
     */
    protected abstract void onInflateView();

    /**
     * 查找控件
     */
    protected abstract void onFindView();

    /**
     * 设置控件相关信息
     */
    protected abstract void onSetupView();

    /**
     * 聊天气泡点击事件
     */
    protected abstract void onBubbleClick();

    /**
     * 聊天气泡长按事件
     */
    protected abstract void onBubbleLongClick();

    public static void setAgentNickAndAvatar(Context context, Message message, ImageView userAvatarView, TextView usernickView){
        AgentInfo agentInfo = MessageHelper.getAgentInfo(message);
        if (usernickView != null){
            usernickView.setText(message.from());
            if (agentInfo != null){
                if (!TextUtils.isEmpty(agentInfo.getNickname())) {
                    usernickView.setText(agentInfo.getNickname());
                }
            }
        }
        if (userAvatarView != null){
            userAvatarView.setImageResource(R.mipmap.custom_service1);
            if (agentInfo != null){
                if (!TextUtils.isEmpty(agentInfo.getAvatar())) {
                    String strUrl = agentInfo.getAvatar();
                    if (!TextUtils.isEmpty(strUrl)) {
                        if (!strUrl.startsWith("http")) {
                            strUrl = "http:" + strUrl;
                        }
                        Glide.with(context).load(strUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.custom_service1).transform(new GlideCircleTransform(context)).into(userAvatarView);
                    }
                }
            }

        }
    }

}
