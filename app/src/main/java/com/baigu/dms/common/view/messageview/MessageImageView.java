package com.baigu.dms.common.view.messageview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.circleimage.transfer.glideloader.GlideImageLoader;
import com.baigu.dms.common.view.circleimage.transfer.style.progress.ProgressPieIndicator;
import com.baigu.dms.common.view.circleimage.transfer.transfer.TransferConfig;
import com.baigu.dms.common.view.circleimage.transfer.transfer.Transferee;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public class MessageImageView extends MessageView {

    private LinearLayout mContentLayout;
    private int mMaxImageSize;
    private List<ImageView> mIvList = new ArrayList<>();
    private List<String> mUrlList = new ArrayList<>();

    public MessageImageView(Context context, Message message, int position) {
        super(context, message, position);
    }

    @Override
    protected void onInflateView() {
        int layoutId = isSelfSend() ? R.layout.item_message_image_to : R.layout.item_message_image_from;
        mInflater.inflate(layoutId, this);
        mMaxImageSize = (int) (ViewUtils.getScreenInfo(getContext()).widthPixels
                - getContext().getResources().getDimension(R.dimen.message_item_head_size) * 2
                - getContext().getResources().getDimension(R.dimen.message_item_head_margin) * 2
                - ViewUtils.dip2px(50));
    }

    @Override
    protected void onFindView() {
        mContentLayout = findView(R.id.ll_content);
    }

    @Override
    protected void onSetupView() {
        mContentLayout.removeAllViews();
        View picView = mInflater.inflate(R.layout.item_message_image_pic, null);
        ImageView ivContent = (ImageView) picView.findViewById(R.id.iv_content);
        mIvList.clear();
        mIvList.add(ivContent);
        mUrlList.clear();
        if (mMessage.getBody() instanceof EMImageMessageBody) {
            EMImageMessageBody imgBody = (EMImageMessageBody) mMessage.getBody();
            String url = "";
            if (!TextUtils.isEmpty(imgBody.getLocalUrl()) && mMessage.direct() == Message.Direct.SEND) {
                Uri uri = Uri.fromFile(new File(imgBody.getLocalUrl()));
                url = uri.toString();
            } else {
                url = TextUtils.isEmpty(imgBody.getThumbnailUrl()) ? imgBody.getRemoteUrl() : imgBody.getThumbnailUrl();
            }
            mUrlList.add(url);
            Glide.with(getContext()).load(url).asBitmap().placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(ivContent) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    int realWidth = resource.getWidth();
                    int realHeigth = resource.getHeight();
                    int scaledWidth = realWidth;
                    int scaledHeight = realHeigth;

                    if (realWidth > mMaxImageSize || realHeigth > mMaxImageSize) {
                        float widthRate = mMaxImageSize * 1.0F / realWidth;
                        float heightRate = mMaxImageSize * 1.0F / realHeigth;
                        float minRate = Math.min(widthRate, heightRate);
                        scaledWidth = (int) (realWidth * minRate);
                        scaledHeight = (int) (realHeigth * minRate);
                    }
                    view.getLayoutParams().width = scaledWidth;
                    view.getLayoutParams().height = scaledHeight;
                    view.requestLayout();
                }
            });
        }

        mContentLayout.addView(picView);
    }

    @Override
    protected void onBubbleClick() {
        final Activity activity = (Activity) mContext;
        TransferConfig config = TransferConfig.build()
                .setNowThumbnailIndex(0)
                .setSourceImageList(mUrlList)
                .setMissPlaceHolder(R.mipmap.pictures_placeholder)
                .setOriginImageList(mIvList)
                .setOffscreenPageLimit(3)
                .setProgressIndicator(new ProgressPieIndicator())
                .setImageLoader(GlideImageLoader.with(activity))
                .create();
        Transferee.getDefault(activity).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
            @Override
            public void onShow() {
                Glide.with(activity).pauseRequests();
            }

            @Override
            public void onDismiss() {
                Glide.with(activity).resumeRequests();
            }
        });
    }

    @Override
    protected void onBubbleLongClick() {

    }
}
