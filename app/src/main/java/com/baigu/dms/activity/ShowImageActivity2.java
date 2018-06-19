package com.baigu.dms.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ShowImageActivity2 extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimage2);
        ImageView imageView = findView(R.id.imageView);
        String imageurl = getIntent().getStringExtra("imageurl");
        Glide.with(this).load(imageurl).placeholder(R.mipmap.place_holder).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageView);
    }
}
