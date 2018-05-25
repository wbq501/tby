package com.baigu.dms.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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

import java.util.ArrayList;
import java.util.List;


public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<String> imagesList;

    private List<ImageView> mImageViewList = new ArrayList<>();

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = ViewUtils.dip2px(3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private OnItemClickListener mOnItemClickListener;
    private Transferee mTransferee;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<String> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }

        initView();
    }

    public void setTransferee(Transferee transferee) {
        mTransferee = transferee;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicPara = new LayoutParams(wrap, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        mImageViewList.clear();
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        if (imagesList.size() == 1) {
            ImageView imageView = createImageView(0, false);
            addView(imageView);
            mImageViewList.add(imageView);
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT
                    + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
                        : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    ImageView imageView = createImageView(position, true);

                    rowLayout.addView(imageView);
                }
            }
        }
    }

    private ImageView createImageView(int position, final boolean isMultiImage) {
        String url = imagesList.get(position);
        final ImageView imageView = new ImageView(getContext());
        mImageViewList.add(imageView);
        imageView.setOnClickListener(new ImageOnClickListener(position));
        imageView.setId(url.hashCode());
        if (isMultiImage) {
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
            imageView.setBackgroundColor(getResources().getColor(R.color.font_color_text_hint));
            Glide.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);

            Glide.with(getContext()).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    int realWidth = resource.getWidth();
                    int realHeigth = resource.getHeight();
                    int scaledWidth = realWidth;
                    int scaledHeight = realHeigth;

                    DisplayMetrics metrics = ViewUtils.getScreenInfo(getContext());

                    int maxWidth = metrics.widthPixels - ViewUtils.dip2px(80);
                    int maxHeight = (int) (metrics.heightPixels * 0.6);
                    if (realWidth > maxWidth || realHeigth > maxHeight) {
                        float widthRate = maxWidth * 1.0F / realWidth;
                        float heightRate = maxHeight * 1.0F / realHeigth;
                        float minRate = Math.min(widthRate, heightRate);
                        scaledWidth = (int) (realWidth * minRate);
                        scaledHeight = (int) (realHeigth * minRate);
                    }
                    view.getLayoutParams().width = scaledWidth;
                    view.getLayoutParams().height = scaledHeight;
                    int[] size = new int[2];
                    size[0] = realWidth;
                    size[1] = realHeigth;
                    view.setTag(size);
                    view.requestLayout();
                }
            });
        }


        return imageView;
    }

    private class ImageOnClickListener implements OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            TransferConfig config = TransferConfig.build()
                    .setNowThumbnailIndex(position)
                    .setSourceImageList(imagesList)
                    .setMissPlaceHolder(R.mipmap.ic_empty_photo)
                    .setOriginImageList(mImageViewList)
                    .setProgressIndicator(new ProgressPieIndicator())
                    .setImageLoader(GlideImageLoader.with(getContext()))
                    .create();
            mTransferee.apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                @Override
                public void onShow() {
                    Glide.with(getContext()).pauseRequests();
                }

                @Override
                public void onDismiss() {
                    Glide.with(getContext()).resumeRequests();
                }
            });
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}