package com.baigu.dms.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/26 23:31
 */
public class PermissionDialog extends Dialog implements View.OnClickListener {

    private TextView mTvTip;
    private boolean mCancelable = true;
    private PermissionDialogListener mPermissionDialogListener;

    public PermissionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public PermissionDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public PermissionDialog(Context context) {
        super(context);
        init();
    }

    public void setCancelable(boolean b) {
        setCanceledOnTouchOutside(b);
        mCancelable = b;
    }

    public void setPermissionDialogListener(PermissionDialogListener listener) {
        mPermissionDialogListener = listener;
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_permission_dialog, null);
        view.setOnClickListener(this);
        setContentView(view);
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        int screenWidth = ViewUtils.getScreenInfo(getContext()).widthPixels;
        int width = screenWidth - ViewUtils.dip2px(25) * 2;
        lp.width = width ; //设置宽度
    }

    public void setTip(int resId) {
        mTvTip.setText(resId);
    }

    public void setTip(String text) {
        mTvTip.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (mCancelable) {
            dismiss();
        }
        if (v.getId() == R.id.tv_cancel) {
            if (mPermissionDialogListener != null) {
                mPermissionDialogListener.onCancle();
            }

        } else if (v.getId() == R.id.tv_setting) {
            mPermissionDialogListener.onSetting();
        }
    }

    public interface PermissionDialogListener {
        void onCancle();
        void onSetting();
    }

    @Override
    public void onBackPressed() {

    }
}