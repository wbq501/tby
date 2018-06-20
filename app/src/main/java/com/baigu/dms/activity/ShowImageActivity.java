package com.baigu.dms.activity;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.baigu.dms.R;
import com.baigu.dms.adapter.ShowImageAdapter;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class ShowImageActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ShowImageAdapter adapter;
    private TextView tv_num;
    private TextView tv_save;
    private ImageView centerIv;

    private View curPage;
    private int[] initialedPositions = null;
    private int curPosition = -1;

    private ObjectAnimator objectAnimator;
    ArrayList<String> imagedata = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        initView();
    }

    private void initView() {
        imagedata = getIntent().getStringArrayListExtra("data");
        int position = getIntent().getIntExtra("position", 0);
        initialedPositions = new int[imagedata.size()];
        initInitialedPositions();

        mViewPager = findViewById(R.id.vp_shop);
        tv_num = findView(R.id.tv_num);
        tv_save = findView(R.id.tv_save);
        centerIv = findViewById(R.id.centerIv);
        adapter = new ShowImageAdapter(this);
        adapter.setData(imagedata);
//        mViewPager.setAdapter(adapter);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imagedata.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                if (imagedata != null && imagedata.size() > 0) {
                    final PhotoView view = new PhotoView(ShowImageActivity.this);
                    view.enable();
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    Glide.with(ShowImageActivity.this).load(imagedata.get(position)).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).fitCenter().crossFade()
                    Glide.with(ShowImageActivity.this)
                            .load(imagedata.get(position))
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .fitCenter()
                            .crossFade()
//                            .placeholder(R.mipmap.place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            showErrorLoading();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            occupyOnePosition(position);
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            return false;
                        }
                    }).into(view);
                    container.addView(view);
                    return view;
                }
                return null;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                releaseOnePosition(position);
                container.removeView((View) object);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                curPage = (View) object;
            }
        });
        curPosition = position;
        mViewPager.setCurrentItem(position);
        mViewPager.setTag(curPosition);
        if (initialedPositions[curPosition] != curPosition) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
            showLoadingAnimation();
        }
        tv_num.setText((position+1)+"/"+imagedata.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (initialedPositions[position] != position) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoadingAnimation();
                } else {
                    hideLoadingAnimation();
                }
                curPosition = position;
                tv_num.setText((position+1)+"/"+imagedata.size());
                mViewPager.setTag(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewGroup containerTemp = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
//                if (containerTemp == null){
//                    return;
//                }
//                PhotoView photoViewTemp = (PhotoView) containerTemp.getChildAt(0);
                String appName = getString(R.string.app_name);
                String tip = getString(R.string.permission_sd_camera, appName, appName);
                PermissionRequest permissionRequest = new PermissionRequest(ShowImageActivity.this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
                    @Override
                    public void onGrant() {
                        SaveImage();
                    }
                });
                permissionRequest.requestPermission(false);
            }
        });
    }

    private void SaveImage() {
        PhotoView photoViewTemp = (PhotoView) curPage;
        if (photoViewTemp != null){
            GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) photoViewTemp.getDrawable();
            if (glideBitmapDrawable == null){
                return;
            }
            Bitmap bitmap = glideBitmapDrawable.getBitmap();
            if (bitmap == null){
                return;
            }
            FileUtils.savePhoto(ShowImageActivity.this, bitmap, new FileUtils.SaveResultCallback() {
                @Override
                public void onSavedSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShowImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onSavedFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShowImageActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void occupyOnePosition(int position) {
        initialedPositions[position] = position;
    }

    private void initInitialedPositions() {
        for (int i = 0; i < initialedPositions.length; i++) {
            initialedPositions[i] = -1;
        }
    }

    private void showLoadingAnimation() {
        centerIv.setVisibility(View.VISIBLE);
        centerIv.setImageResource(R.drawable.loading);
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(centerIv, "rotation", 0f, 360f);
            objectAnimator.setDuration(2000);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                objectAnimator.setAutoCancel(true);
            }
        }
        objectAnimator.start();
    }

    private void hideLoadingAnimation() {
        releaseResource();
        centerIv.setVisibility(View.GONE);
    }

    private void showErrorLoading() {
        centerIv.setVisibility(View.VISIBLE);
        releaseResource();
        centerIv.setImageResource(R.drawable.load_error);
    }

    private void releaseResource() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (centerIv.getAnimation() != null) {
            centerIv.getAnimation().cancel();
        }
    }

    @Override
    protected void onDestroy() {
        releaseResource();
        curPage = null;
        if (mViewPager != null) {
            mViewPager.removeAllViews();
            mViewPager = null;
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.show_img_close);
    }

    private void releaseOnePosition(int position) {
        initialedPositions[position] = -1;
    }
}
