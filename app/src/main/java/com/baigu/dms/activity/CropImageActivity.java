package com.baigu.dms.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.clip.ClipImageLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 13:52
 */
public class CropImageActivity extends BaseActivity {
    private ClipImageLayout mClipImageLayout;
    private ImageView mRotateImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setBackgroundDrawableResource(R.color.black);
        }
        setContentView(R.layout.activity_crop_image);
        initToolBar();
        mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
        setTitle(R.string.translate_or_scale);

        mRotateImgBtn = (ImageView) findViewById(R.id.rotate_image);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.cliplayout);
        Uri data = getIntent().getData();
        if (data == null || !(new File(data.getPath()).exists())) {
            ViewUtils.showToastInfo(R.string.img_load_fail);
            return;
        }
        Bitmap bitmap = convertToBitmap(data.getPath(), 600, 600);
        if (bitmap == null) {
            ViewUtils.showToastInfo(R.string.img_load_fail);
            return;
        }
        mClipImageLayout.setBitmap(bitmap);
        mRotateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipImageLayout.getZoomImageView().initImage(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Bitmap bitmap = mClipImageLayout.clip();
            File outputCropFile = FileUtils.createOutputCropFile();
            String path = outputCropFile.getAbsolutePath();
            File file = shearHeadImg(bitmap, path);
            bitmap.recycle();
            Intent intent = new Intent();
            intent.setData(Uri.parse(file.getAbsolutePath()));
            setResult(RESULT_OK, intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public File shearHeadImg(Bitmap bmp, String path) {
        byte[] btArray = Bitmap2Bytes(bmp);
        int rate = 4;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = rate;
        Bitmap compressedBmp = BitmapFactory.decodeByteArray(btArray, 0, btArray.length, opts);
        File dir = new File(path).getParentFile();
        if (!dir.exists())
            dir.mkdirs();
        savePhotoToSDCard(getCircleBitmap(compressedBmp), path);
        return new File(path);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static void savePhotoToSDCard(Bitmap photoBitmap, String path) {
        if (FileUtils.isExistExternalStore()) {
            File photoFile = new File(path);
            File dir = photoFile.getParentFile();
            if (!dir.exists())
                dir.mkdirs();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (Exception e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    public static final Bitmap convertToBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 设置为ture只获取图片大小
            opts.inJustDecodeBounds = true;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            // 返回为空
            BitmapFactory.decodeFile(path, opts);
            int width = opts.outWidth;
            int height = opts.outHeight;
            float scaleWidth = 0.f, scaleHeight = 0.f;
            if (width > w || height > h) {
                // 缩放
                scaleWidth = ((float) width) / w;
                scaleHeight = ((float) height) / h;
            }
            opts.inJustDecodeBounds = false;
            float scale = Math.max(scaleWidth, scaleHeight);
            opts.inSampleSize = (int) scale;
            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
            Bitmap bMapRotate = Bitmap.createBitmap(weak.get(), 0, 0, weak.get().getWidth(), weak.get().getHeight(), null, true);
            if (bMapRotate != null) {
                return bMapRotate;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}