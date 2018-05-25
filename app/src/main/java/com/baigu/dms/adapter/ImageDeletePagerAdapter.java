package com.baigu.dms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.salvageviewpager.RecyclingPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDeletePagerAdapter extends RecyclingPagerAdapter implements PhotoViewAttacher.OnPhotoTapListener, PhotoViewAttacher.OnViewTapListener {

    private Context mContext;
    private boolean mAbleLongClick;
    public List<String> mDataList;

    private ImagePagerListener mImagePagerListener;

    public ImageDeletePagerAdapter(Context context) {
        mContext = context;
    }

    public void ableLongClick(boolean b) {
        mAbleLongClick = true;
    }

    public void setData(List<String> t) {
        mDataList = t;
    }

    public void setImagePagerListener(ImagePagerListener listener) {
        mImagePagerListener = listener;
    }

    @Override
    public int getCount() {
        return mDataList == null || mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        PhotoView photoView = (PhotoView) convertView;
        if (photoView == null) {
            photoView = new PhotoView(mContext);
            photoView.setOnPhotoTapListener(this);
            photoView.setOnViewTapListener(this);
        }
        String url = mDataList.get(position);
        if (mAbleLongClick) {
            photoView.setOnLongClickListener(new OnImageLongClickListener(url));
        }

        Glide.with(mContext).load(url).placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(new ViewTarget<ImageView, GlideDrawable>(photoView) {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                view.setImageDrawable(placeholder);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                if (resource == null && mImagePagerListener != null) {
                    mImagePagerListener.onLoadedImage(false);
                }
                this.view.setImageDrawable(resource);
                int realWidth = resource.getIntrinsicWidth();
                int realHeigth = resource.getIntrinsicHeight();
                DisplayMetrics metrics = ViewUtils.getScreenInfo(mContext);

                float widthRate = realWidth * 1.0F / metrics.widthPixels;
                float heightRate = realHeigth * 1.0F / metrics.heightPixels;
                float maxRate = Math.max(widthRate, heightRate);
                PhotoView pView = (PhotoView) view;
                if (maxRate > pView.getMinimumScale()) {
                    maxRate = pView.getMinimumScale();
                }
                if (pView.getIPhotoViewImplementation() != null) {
                    ((PhotoView) view).setMinimumScale(maxRate);
                }
                if (mImagePagerListener != null) {
                    mImagePagerListener.onLoadedImage(true);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (mImagePagerListener != null) {
                    mImagePagerListener.onLoadedImage(false);
                }
            }
        });
        photoView.setBackgroundColor(Color.BLACK);
        return photoView;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (mImagePagerListener != null) {
            mImagePagerListener.onImageClick(view);
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (mImagePagerListener != null) {
            mImagePagerListener.onImageClick(view);
        }
    }

    @Override
    public void onOutsidePhotoTap() {
        if (mImagePagerListener != null) {
            mImagePagerListener.onImageClick(null);
        }
    }

    class OnImageLongClickListener implements OnLongClickListener {
        public String url;

        public OnImageLongClickListener(String url) {
            this.url = url;
        }

        @Override
        public boolean onLongClick(View v) {
            if (mImagePagerListener != null) {
                mImagePagerListener.onLongCLicked(url);
            }
            return false;
        }
    }

    public interface ImagePagerListener {
        /**开始加载图片*/
        void onStartLoadImage();

        /**
         * 图片加载完成
         * @param isSuccess true-成功 false-失败
         */
        void onLoadedImage(boolean isSuccess);

        /**
         * 图片单击事件
         * @param v
         */
        void onImageClick(View v);

        /**
         * 图片长按
         */
        void onLongCLicked(String url);
    }
}