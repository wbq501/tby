package com.baigu.dms.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.baigu.dms.R;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.common.utils.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class ShowImageAdapter extends PagerAdapter {

    private Activity context;
    private List<String> data;
    private View[] views;

    public ShowImageAdapter(Activity context) {
        this.context = context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public void setData(List<String> data) {
        this.data = data;
        views = new View[data.size()];
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views[position]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_show_image, container, false);
        ImageView imageView = view.findViewById(R.id.iv_img);
//        ImageUtil.loadImage(context, data.get(position), imageView);
        Glide.with(context).load(data.get(position)).placeholder(R.mipmap.place_holder).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageView);
        views[position]=view;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });
        container.addView(view);
        return view;
    }
}
