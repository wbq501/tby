package com.baigu.dms.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class PayPasswordDialog extends Dialog {

    private TextView mTvCancle;
    private TextView mTvSubmit;
    private EditText mEtPwd;
    private OnSubmitClickListener clickListener;

    public PayPasswordDialog(@NonNull Context context) {
        super(context, R.style.ConfirmDialog);
        initView(context);
    }

    public PayPasswordDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected PayPasswordDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }


    private void initView(Context context) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
        lp.height = 600;
        getWindow().setAttributes(lp);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_pay_password, null);
        setContentView(view);
        mTvCancle = view.findViewById(R.id.tv_cancel);
        mTvSubmit = view.findViewById(R.id.tv_submit);
        mEtPwd = view.findViewById(R.id.et_pwd);

        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
        clickListener = onSubmitClickListener;
        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(mEtPwd.getText().toString());
            }
        });

    }


    public interface OnSubmitClickListener {
        void onClick(String pwd);
    }
}
