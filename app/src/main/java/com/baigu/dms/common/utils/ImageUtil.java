package com.baigu.dms.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class ImageUtil {


    public static void loadImage(Context context, String  url, ImageView imageView){
        Glide.with(context).load(url).centerCrop().placeholder(R.mipmap.place_holder).into(imageView);
    }


}
