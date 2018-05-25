package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/18 22:51
 */
public class StepView extends FrameLayout {
    private ImageView mIvStep1;
    private ImageView mIvStep2;
    private ImageView mIvStep3;
    private TextView mTvStep1;
    private TextView mTvStep2;
    private TextView mTvStep3;

    private int mImg1ResId;
    private int mImg1PressedResId;
    private String mText1;

    private int mImg2ResId;
    private int mImg2PressedResId;
    private String mText2;

    private int mImg3ResId;
    private int mImg3PressedResId;
    private String mText3;

    public StepView(@NonNull Context context) {
        super(context);
        initView();
    }

    public StepView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StepView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_step, this);
        mIvStep1 = (ImageView) findViewById(R.id.iv_step1);
        mIvStep2 = (ImageView) findViewById(R.id.iv_step2);
        mIvStep3 = (ImageView) findViewById(R.id.iv_step3);

        mTvStep1 = (TextView) findViewById(R.id.tv_step1);
        mTvStep2 = (TextView) findViewById(R.id.tv_step2);
        mTvStep3 = (TextView) findViewById(R.id.tv_step3);
    }

    public void setImgRes(int img1ResId, int img1PressResId, int img2ResId, int img2PressResId, int img3ResId, int img3PressResId) {
        mImg1ResId = img1ResId;
        mImg1PressedResId = img1PressResId;
        mImg2ResId = img2ResId;
        mImg2PressedResId = img2PressResId;
        mImg3ResId = img3ResId;
        mImg3PressedResId = img3PressResId;

        mIvStep1.setImageResource(mImg1ResId);
        mIvStep2.setImageResource(mImg2ResId);
        mIvStep3.setImageResource(mImg3ResId);
    }

    public void setText(String text1, String text2, String text3) {
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mTvStep1.setText(text1);
        mTvStep2.setText(text2);
        mTvStep3.setText(text3);
    }

    public void setCurStep(int step) {
        switch (step) {
            case 0:
                mIvStep1.setImageResource(mImg1PressedResId);
                mTvStep1.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                mIvStep2.setImageResource(mImg2PressedResId);
                mTvStep2.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                mIvStep3.setImageResource(mImg3PressedResId);
                mTvStep3.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            default:
                break;
        }
    }
}
