package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.circleimage.adapter.NineGridAdapter;
import com.baigu.dms.common.view.circleimage.view.NineGridImageView;
import com.baigu.dms.domain.model.BrandStory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/17 14:06
 */
public class BrandStoryDetailView extends FrameLayout {

    private ImageView mIvHead;
    private TextView mTvUsername;
    private TextView mTvContent;
    private TextView mTvTime;
    private NineGridImageView mNineGridImageView;

    private TabLayout mTabLayout;

    private OnCommentPraiseTabChangeListener mOnCommentPraiseTabChangeListener;

    private BrandStory mBrandStory;

    public BrandStoryDetailView(@NonNull Context context) {
        super(context);
        initView();
    }

    public BrandStoryDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BrandStoryDetailView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setOnCommentPraiseTabChangeListener(OnCommentPraiseTabChangeListener listener) {
        mOnCommentPraiseTabChangeListener = listener;
    }

    public void setBrandStory(BrandStory brandStory) {
        mBrandStory = brandStory;
        loadData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_brand_story_detail, this);
        mIvHead = (ImageView) findViewById(R.id.iv_head);
        mTvUsername = (TextView) findViewById(R.id.tv_username);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mTvTime = (TextView) findViewById(R.id.tv_time);

        mNineGridImageView = (NineGridImageView) findViewById(R.id.nineGridImageView);
        mNineGridImageView.setMaxSize(9);
        NineGridAdapter nineGridAdapter  = new NineGridAdapter((Activity)getContext());
        mNineGridImageView.setAdapter(nineGridAdapter);
        int singleImgWidth = ViewUtils.getScreenInfo(getContext()).widthPixels - 2 * ViewUtils.dip2px(20);
        mNineGridImageView.setSingleImgWidth(singleImgWidth);



        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.main_text), getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(ViewUtils.dip2px(1.5f));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.comment));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.praise));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mOnCommentPraiseTabChangeListener != null) {
                    mOnCommentPraiseTabChangeListener.onCommentPraiseTabSelect(tab.getPosition() == 0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadData() {
        Glide.with(getContext()).load(mBrandStory.getBrand_img()).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getContext())).into(mIvHead);
        mTvUsername.setText(mBrandStory.getBrand_title());
        mTvContent.setText(mBrandStory.getBrand_content());
        mTvTime.setText(StringUtils.getTimeLabelStr(mBrandStory.getCreate_time()));
        mNineGridImageView.setImagesData(mBrandStory.getUrlList());
    }

    public interface OnCommentPraiseTabChangeListener {
        void onCommentPraiseTabSelect(boolean commentTab);
    }

}
