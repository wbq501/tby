package com.baigu.dms.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.view.gestureLock.GestureContentView;
import com.baigu.dms.common.view.gestureLock.GestureDrawline;
import com.baigu.dms.common.view.gestureLock.GestureSuccessDialog;
import com.baigu.dms.common.view.gestureLock.LockIndicator;
import com.baigu.dms.domain.cache.UserCache;

/**
 * @Description 手势锁
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class GestureEditActivity extends BaseActivity implements View.OnClickListener {

    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true; //手势锁永远第一次有效
    // ----------测试加上的----------
    private int countInput = 0;           //每偶数个手势锁有效
    private String mFirstPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        initToolBar();
        setTitle(R.string.setup_gesture_code);
        initView();
    }

    private void initView() {

        mTextReset = (TextView) findViewById(R.id.text_reset);
        mTextReset.setClickable(false);
        mTextReset.setOnClickListener(this);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#666666'>最少连接4个点, 请重新输入</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
//                if (mIsFirstInput) {
                if (countInput % 2 == 0) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextReset.setClickable(true);
                    mTextReset.setText(getString(R.string.reset_gesture_code));
                    mTextTip.setText(Html
                            .fromHtml("<font color='##666666'>请再次绘制解锁图案</font>"));
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        mTextTip.setText(Html
                                .fromHtml("<font color='##666666'>设置成功</font>"));
                        if (inputCode != null) {
                            UserCache.getInstance().setGesture(inputCode);
                            GestureSuccessDialog dialog = new GestureSuccessDialog(GestureEditActivity.this);
                            dialog.show();
                        }
                        mGestureContentView.clearDrawlineState(0L);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(500);
                                GestureEditActivity.this.finish();
                            }
                        }).start();
//
                    } else {
                        updateCodeList("");
                        mTextTip.setText(Html.fromHtml("<font " +
                                "color='##666666'>与上次输入不一致，请重新设置</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation
                                (GestureEditActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                countInput++;
//                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

    private void setUpListeners() {
        mTextReset.setOnClickListener(this);
    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_reset) {
            updateCodeList("");
            mIsFirstInput = true;
            mTextTip.setText(getString(R.string.set_gesture_pattern));
        }
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

}
