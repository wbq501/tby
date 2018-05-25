package com.baigu.dms.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;


public class LoadingDialog extends Dialog {

    private TextView mLoadingText;

    private String message = null;
    private Activity mActivity;

    public LoadingDialog(Activity activity) {
        this(activity, activity.getString(R.string.loading));
        mActivity = activity;
    }

    public LoadingDialog(Context context, String message) {
        super(context, R.style.LoadingDialogStyle);
        this.message = message;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.view_tips_loading, null);
        this.setContentView(view);
        mLoadingText = (TextView) findViewById(R.id.tips_loading_msg);
        mLoadingText.setText(this.message);
        setCanceledOnTouchOutside(false);
    }

    public void setText(String message) {
        this.message = message;
        mLoadingText.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mActivity != null) {
            if (isShowing()) {
                dismiss();
            }
            mActivity.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
