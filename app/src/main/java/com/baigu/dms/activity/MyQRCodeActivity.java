package com.baigu.dms.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class MyQRCodeActivity extends BaseActivity {

    private ImageView mIvQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);
        initToolBar();
        setTitle(R.string.my_qrcode);

        initView();
    }

    private void initView() {
        mIvQrCode = findView(R.id.iv_qrcode);
        Glide.with(this).load("https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=fa9140accd95d143ce7bec711299e967/2934349b033b5bb571dc8c5133d3d539b600bc12.jpg").diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvQrCode);
    }
}
