package com.baigu.dms.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.clipviewpager.ClipViewPager;
import com.baigu.dms.common.view.clipviewpager.ScalePageTransformer;
import com.baigu.dms.common.view.salvageviewpager.RecyclingPagerAdapter;
import com.baigu.dms.domain.model.Advert;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public class BannerView extends FrameLayout {

    private int maxCount = 0;
    private ImageView mIvSingle;
    private ClipViewPager mViewPager;
    private View mViewPagerContainer;
    private OnBannerItemClickListener mOnBannerItemClickListener;

    private BannerPageAdapter mBannerPageAdapter;

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        LayoutInflater.from(getContext()).inflate(R.layout.view_banner, this);
        mIvSingle = (ImageView) findViewById(R.id.iv_single);
        mViewPagerContainer = findViewById(R.id.page_container);
        mViewPagerContainer.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;


        mViewPager = (ClipViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(ViewUtils.dip2px(20) * -1);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());

        int width = ViewUtils.getScreenInfo(context).widthPixels - ViewUtils.dip2px(30);
        mViewPager.getLayoutParams().width = width;
        mViewPager.getLayoutParams().height = (int) (width * 0.3);

        mIvSingle.getLayoutParams().width = ViewUtils.getScreenInfo(context).widthPixels - ViewUtils.dip2px(20);
        mIvSingle.getLayoutParams().height = mViewPager.getLayoutParams().height;

        mBannerPageAdapter = new BannerPageAdapter();
        mViewPager.setAdapter(mBannerPageAdapter);
    }

    public void setOnItemClickListener(OnBannerItemClickListener listener) {
        mOnBannerItemClickListener = listener;
    }

    public void setData(final List<Advert> list) {

        if (list == null || list.size() <= 0) {
            mViewPagerContainer.setVisibility(View.GONE);
            return;
        }
        mViewPagerContainer.setVisibility(View.VISIBLE);
        mIvSingle.setOnClickListener(null);
        if (list.size() == 1) {
            mViewPager.setVisibility(View.GONE);
            mIvSingle.setVisibility(View.VISIBLE);
            mIvSingle.setOnClickListener(new OnBannerClickListener(list.get(0)));
            mViewPagerContainer.setOnTouchListener(null);
            Glide.with(getContext()).load(list.get(0).getAdvertis_img()).transform(new CenterCrop(getContext()), new GlideRoundTransform(getContext(), 5)).into(mIvSingle);
        } else {
            mViewPager.setVisibility(View.VISIBLE);
            mIvSingle.setVisibility(View.GONE);
            mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mViewPager.dispatchTouchEvent(event);
                }
            });

            maxCount = list.size() * 100;

            mBannerPageAdapter.setData(list);
            mBannerPageAdapter.notifyDataSetChanged();

            int curr = maxCount / 2 - 1;
            //解决显示问题
            mViewPager.setCurrentItem(curr, false);
            mViewPager.setCurrentItem(curr + 1, true);
        }
    }

    protected final class BannerPageAdapter extends RecyclingPagerAdapter {

        private List<Advert> dataList;

        public void setData(List<Advert> list) {
            this.dataList = list;
        }

        @Override
        public int getCount() {
            return dataList == null || dataList.size() == 0 ? 0 : maxCount;
        }

        public Advert getItem(int position) {
            return dataList == null ? null : dataList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            Holder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_banner, null);
                holder = new Holder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.position = position;
            Advert advert = dataList.get(position % dataList.size());
            holder.imageView.setOnClickListener(new OnBannerClickListener(advert));
            Glide.with(getContext()).load(advert.getAdvertis_img()).placeholder(R.mipmap.place_holder).transform(new CenterCrop(getContext()), new GlideRoundTransform(getContext(), 5)).into(holder.imageView);
            return convertView;
        }

        class Holder extends ClipViewPager.ClipViewHolder {
            ImageView imageView;
        }
    }

    class OnBannerClickListener implements View.OnClickListener {

        private Advert advert;

        public OnBannerClickListener(Advert advert) {
            this.advert = advert;
        }

        @Override
        public void onClick(View v) {
            if (mOnBannerItemClickListener != null) {
                mOnBannerItemClickListener.onItemClick(v, advert);
            }
        }
    }

    public interface OnBannerItemClickListener {
        void onItemClick(View view, Advert advert);
    }

}
