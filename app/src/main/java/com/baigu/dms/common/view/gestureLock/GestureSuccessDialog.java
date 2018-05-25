package com.baigu.dms.common.view.gestureLock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.baigu.dms.R;


public class GestureSuccessDialog extends Dialog {


    public GestureSuccessDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
    }

    public GestureSuccessDialog(Context context, int themeResId) {
        super(context,  R.style.LoadingDialogStyle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.view_gesture_success_dialog);
    }

}
