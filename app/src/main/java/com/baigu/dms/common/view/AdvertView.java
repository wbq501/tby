package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Advert;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class AdvertView extends FrameLayout implements OnBannerListener {

    public static final int ADVERT_INTERVAL_TIME = 6000;

    private OnAdvertItemClickListener mOnAdvertItemClickListener;

    private Banner mBanner;
    private List<Advert> mAdList = new ArrayList<>();

    public AdvertView(@NonNull Context context) {
        super(context);
        initView();
    }

    public AdvertView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AdvertView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_advert, this);
        mBanner = (Banner) findViewById(R.id.banner);

        DisplayMetrics dm = ViewUtils.getScreenInfo(getContext());
        mBanner.getLayoutParams().height = (int) (dm.widthPixels * 0.5);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(ADVERT_INTERVAL_TIME);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setOnBannerListener(this);
    }

    public void setData(List<Advert> adList) {
        if (adList == null || adList.size() <= 0) {
            mAdList.clear();
            mBanner.setImages(mAdList);
            setVisibility(View.GONE);
            return;
        }
        mAdList.clear();
        mAdList.addAll(adList);
        List<String> list = new ArrayList<>();
        for (Advert advert : mAdList) {
            list.add(advert.getAdvertis_img());
        }
        mBanner.setImages(list);
        mBanner.start();
    }

    public void setOnItemClickListener(OnAdvertItemClickListener listener) {
        mOnAdvertItemClickListener = listener;
    }

    @Override
    public void OnBannerClick(int position) {
        if (mAdList == null || position >= mAdList.size()) {
            return;
        }
        if (mOnAdvertItemClickListener != null) {
            mOnAdvertItemClickListener.onItemClick(mAdList.get(position));
        }
    }

    public interface OnAdvertItemClickListener {
        void onItemClick(Advert advert);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(getContext()).load(path).placeholder(R.mipmap.place_holder).centerCrop().into(imageView);
        }
    }
}
