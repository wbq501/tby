package com.baigu.dms.common.view.circleimage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.baigu.dms.common.view.circleimage.transfer.glideloader.GlideImageLoader;
import com.baigu.dms.common.view.circleimage.transfer.style.progress.ProgressPieIndicator;
import com.baigu.dms.common.view.circleimage.transfer.transfer.TransferConfig;
import com.baigu.dms.common.view.circleimage.transfer.transfer.Transferee;
import com.baigu.dms.common.view.circleimage.view.NineGridImageView;
import com.baigu.dms.common.view.circleimage.view.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class NineGridAdapter extends NineGridImageViewAdapter<String> {

    private Activity mActivity;

    private int mPlaceHolderResId = R.mipmap.ic_empty_photo;

    public NineGridAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setPlaceHolderResId(int resId) {
        mPlaceHolderResId = resId;
    }

    @Override
    protected void onDisplayImage(Context context, final NineGridImageView nineGridImageView, final ImageView imageView, String photo, final NineGridImageView.GridInfo info) {
        Glide.with(context).load(photo).placeholder(mPlaceHolderResId).diskCacheStrategy(DiskCacheStrategy.ALL).into(new ViewTarget<ImageView, GlideDrawable>(imageView) {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                view.setImageDrawable(placeholder);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                this.view.setImageDrawable(resource);
                if (info != null && info.singleImage) {
                    int realWidth = resource.getIntrinsicWidth();
                    int realHeigth = resource.getIntrinsicHeight();
                    int scaledWidth = realWidth;
                    int scaledHeight = realHeigth;

                    int maxWidth = info.singleImageWidth;
                    int maxHeight = (int) (maxWidth * 0.6);
                    if (realWidth > maxWidth || realHeigth > maxHeight) {
                        float widthRate = maxWidth * 1.0F / realWidth;
                        float heightRate = maxHeight * 1.0F / realHeigth;
                        float minRate = Math.min(widthRate, heightRate);
                        scaledWidth = (int) (realWidth * minRate);
                        scaledHeight = (int) (realHeigth * minRate);
                    }
                    this.view.layout(0, 0, scaledWidth, scaledHeight);
                    final int height = scaledHeight + nineGridImageView.getPaddingTop() + nineGridImageView.getPaddingBottom();
                    if (info.nowHeight == height) { //已计算不再计算，防止循环刷新
                        return;
                    }
                    this.view.post(new Runnable() {
                        @Override
                        public void run() {
                            nineGridImageView.setHeight(height);
                            nineGridImageView.invalidate();
                            nineGridImageView.requestLayout();
                        }
                    });
                }
                nineGridImageView.requestLayout();
            }
        });
    }

    @Override
    protected void onItemImageClick(Context context, NineGridImageView nineGridImageView, ImageView imageView, int index, List<String> photoList) {
        TransferConfig config = TransferConfig.build()
                .setNowThumbnailIndex(index)
                .setSourceImageList(photoList)
                .setMissPlaceHolder(mPlaceHolderResId)
                .setOriginImageList(nineGridImageView.getImageViewList())
                .setOffscreenPageLimit(3)
                .setProgressIndicator(new ProgressPieIndicator())
                .setImageLoader(GlideImageLoader.with(mActivity))
                .create();
        Transferee.getDefault(mActivity).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
            @Override
            public void onShow() {
                Glide.with(mActivity).pauseRequests();
            }

            @Override
            public void onDismiss() {
                Glide.with(mActivity).resumeRequests();
            }
        });
    }
}
