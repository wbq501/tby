package com.baigu.dms.common.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

import java.util.Observable;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class PayDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private EditText et_money;
    private TextView money;
    private Button confirm;
    private double maxPrice;
    private double payPrice;
    private PayDialogDismissListener dialogDismissListener;

    public double getMoney() {
        return Double.valueOf(et_money.getText().toString());
    }

    public double getPayMoney() {
        return Double.valueOf(et_money.getText().toString());
    }

    public PayDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public PayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected PayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public PayDialogDismissListener getDialogDismissListener() {
        return dialogDismissListener;
    }

    public void setDialogDismissListener(PayDialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    private void initView(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_pay, null);
        et_money = (EditText) view.findViewById(R.id.et_wallet_money);
        money = (TextView) view.findViewById(R.id.tv_wallet_money);
        confirm = (Button) view.findViewById(R.id.bt_confirm);
        confirm.setOnClickListener(this);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        if (maxPrice < payPrice) {
            et_money.setText(maxPrice + "");
            et_money.setSelection(String.valueOf(maxPrice).length());
        } else {
            et_money.setText(payPrice + "");
            et_money.setSelection(String.valueOf(payPrice).length());
        }
        et_money.setEnabled(false);
        money.setText("可用金额:￥" + maxPrice);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                //todo 数据判断
                double payMoney = Double.valueOf(et_money.getText().toString());
                if (payMoney <= 0 && maxPrice > 0) {
                    ViewUtils.showToastError(context.getString(R.string.input_wallet_money));
                    return;
                }
                if (maxPrice - payMoney < 0) {
                    ViewUtils.showToastError(context.getString(R.string.money_unenough));
                    return;
                }

                if (dialogDismissListener != null) {
                    if (payMoney > payPrice) {
                        dialogDismissListener.getMoneyListner(String.valueOf(payPrice));
                    } else {
                        if (maxPrice > 0) {
                            dialogDismissListener.getMoneyListner(et_money.getText().toString());
                        }

                    }

                }

                dismiss();
                break;
        }
    }

    public interface PayDialogDismissListener {
        void getMoneyListner(String money);
    }
}
