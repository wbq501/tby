package com.baigu.dms.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    private boolean mHideCancel;
    private boolean mCloseAble = true;
    private Activity mActivity;
    private TextView mTvTitle;
    private String mTitle;
    private String mOKText;
    private String mCancelText;
    private TextView mTvCancel;
    private View mLineCancel;

    public ConfirmDialog(Activity activity, String title) {
        super(activity, R.style.ConfirmDialog);
        this.mActivity = activity;
        mTitle = title;
    }

    public ConfirmDialog(Activity activity, int titleResId) {
        this(activity, activity.getString(titleResId));
    }

    public ConfirmDialog(Activity activity, String title, boolean closeAble) {
        super(activity);
        this.mActivity = activity;
        mTitle = title;
        mCloseAble = closeAble;
    }

    public ConfirmDialog(Activity activity, int titleResId, boolean closeAble) {
        this(activity, activity.getString(titleResId));
        mCloseAble = closeAble;
    }

    public void setTitleResId(int titleResId) {
        setTitle(mActivity.getString(titleResId));
    }

    public void setTitle(String title) {
        mTitle = title;
        if (mTvTitle != null) {
            mTvTitle.setText(mTitle);
        }
    }

    public void setHideCancel(boolean b) {
        mHideCancel = b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_confirm_dialog);
        setCanceledOnTouchOutside(mCloseAble);
        mTvTitle = (TextView) findViewById(R.id.title);
        mTvTitle.setText(mTitle);
        TextView tvOK = (TextView) findViewById(R.id.tv_ok);
        tvOK.setOnClickListener(this);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        mLineCancel = findViewById(R.id.line_cancel);
        if (!TextUtils.isEmpty(mOKText)) {
            tvOK.setText(mOKText);
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mTvCancel.setText(mCancelText);
        }
        mTvCancel.setVisibility(mHideCancel ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.tv_ok:
                if (mOnConfirmDialogListener != null) {
                    mOnConfirmDialogListener.onOKClick(v);
                }
                break;
            case R.id.tv_cancel:
                if (mOnConfirmDialogListener instanceof OnConfirmDialogListener2) {
                    ((OnConfirmDialogListener2) mOnConfirmDialogListener).onCancelClick(v);
                } else {
                    hide();
                }
                break;
            default:
                hide();
                break;
        }
    }

    public void setOKText(String text) {
        mOKText = text;
    }

    public void setCancelText(String text) {
        mCancelText = text;
    }

    public interface OnConfirmDialogListener {
        void onOKClick(View v);
    }

    public interface OnConfirmDialogListener2 extends OnConfirmDialogListener {
        void onCancelClick(View v);
    }

    private OnConfirmDialogListener mOnConfirmDialogListener;

    public void setOnConfirmDialogListener(OnConfirmDialogListener listener) {
        this.mOnConfirmDialogListener = listener;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (!mCloseAble && keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
