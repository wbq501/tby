package com.baigu.dms.common.view.messageview;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.activity.EvaluationActivity;
import com.hyphenate.chat.Message;

public class MessageEvaluationView extends MessageView {

    public MessageEvaluationView(Context context, Message message, int position) {
        super(context, message, position);
    }

    @Override
    protected void onInflateView() {
        int layoutId = isSelfSend() ? R.layout.item_message_evaluation_to : R.layout.item_message_evaluation_from;
        mInflater.inflate(layoutId, this);
    }

    @Override
    protected void onFindView() {
        findView(R.id.tv_evaluation).setOnClickListener(this);
    }

    @Override
    protected void onSetupView() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_evaluation) {
            Intent intent = new Intent(getContext(), EvaluationActivity.class);
            intent.putExtra("message",  mMessage);
            getContext().startActivity(intent);
        } else {
            super.onClick(v);
        }
    }

    @Override
    protected void onBubbleClick() {

    }

    @Override
    protected void onBubbleLongClick() {

    }
}