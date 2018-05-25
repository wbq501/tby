package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.common.view.gestureLock.GestureContentView;
import com.baigu.dms.common.view.gestureLock.GestureDrawline;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description 手势锁解锁
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/11/17 11:25
 */
public class GestureVerifyActivity extends BaseActivity {

    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private ImageView mIvHead;

    private int psd_count = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        initToolBar();
        setTitle(R.string.gesture_lock);
        initView();
    }

    private void initView() {
        mTextTip = findView(R.id.text_tip);
        mGestureContainer = findView(R.id.gesture_container);
        mIvHead = findView(R.id.iv_head);
        User user = UserCache.getInstance().getUser();
        Glide.with(this).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(mIvHead);

        mGestureContentView = new GestureContentView(this, true, UserCache.getInstance().getGesture(),
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        startActivity(new Intent(GestureVerifyActivity.this, WalletActivity.class));
                        finish();
                    }

                    @Override
                    public void checkedFail() {
                        psd_count--;
                        if (psd_count <= 0) {
                            finish();
                        }
                        mGestureContentView.clearDrawlineState(1300L);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e' >密码错误,还可再试</font>"
                                        + "<font color='#c70c1e'>"
                                        + psd_count
                                        + "</font>"
                                        + "<font color='#c70c1e'>次</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation
                                (GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }
                }

        );
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }


}