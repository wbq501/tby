package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.AddOrderActivity;
import com.baigu.dms.activity.MainActivity;
import com.baigu.dms.common.utils.ShareToQQ;
import com.baigu.dms.common.utils.ShareUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.wxapi.WXShare;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/26 21:10
 */
public class SharePopView extends PopupWindow implements OnClickListener {


    private LinearLayout mLlContainer;
    private View mView;
    private Context mContext;

    public SharePopView(Activity context) {
        super(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.view_share_pop, null);
        mLlContainer = (LinearLayout) mView.findViewById(R.id.ll_container);
        this.setContentView(mView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.GoodsAddPopWindow);
        ColorDrawable dw = new ColorDrawable(0xA0000000);
        mView.setBackground(dw);
        mView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mLlContainer.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        mView.findViewById(R.id.ll_weixin_friend).setOnClickListener(this);
        mView.findViewById(R.id.ll_weixin_circle).setOnClickListener(this);
        mView.findViewById(R.id.ll_qq_friend).setOnClickListener(this);
        mView.findViewById(R.id.ll_qq_zone).setOnClickListener(this);
        mView.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.ll_weixin_friend:
                WXShare.getInstance().shareText(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl(), false);
                break;
            case R.id.ll_weixin_circle:
                WXShare.getInstance().shareText(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl(), true);
                break;
            case R.id.ll_qq_friend:
                ShareToQQ.getInstance(mContext).shareTextToFriend(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl());
                break;
            case R.id.ll_qq_zone:
                ShareToQQ.getInstance(mContext).shareTextToZone(ShareUtils.getShareTitle(), ShareUtils.getShareContent(), ShareUtils.getShareUrl());
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}