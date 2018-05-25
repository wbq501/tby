package com.baigu.dms.common.view.qrcode.core;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class Decoder {

    public static final String TAG = Decoder.class.getSimpleName();

    private  MultiFormatReader mMultiFormatReader;

    private Decoder(Builder builder) {
        mMultiFormatReader = new MultiFormatReader();
    }


    synchronized public String decode(final Bitmap image) {
        final long start = System.currentTimeMillis();
        final int width = image.getWidth(), height = image.getHeight();
        Log.d("crush",width+":"+height);
        final int[] pixels = new int[width * height];
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        final RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result rawResult = mMultiFormatReader.decodeWithState(bitmap);
            final long end = System.currentTimeMillis();
            Log.d(TAG, "QRCode decode in " + (end - start) + "ms");
            Log.d(TAG, rawResult.toString());
            return rawResult.getText();
        } catch (NotFoundException re) {
            Log.w(TAG, re);
            return null;
        } finally {
            mMultiFormatReader.reset();
        }
    }

    public byte[] BitmapToArray(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] data = new int[bmp.getWidth() * bmp.getHeight()];
        byte[] bitmapPixels = new byte[width * height];
        bmp.getPixels(data, 0, width, 0, 0, width, height);
        for (int i = 0; i < data.length; i++) {
            bitmapPixels[i] = (byte) data[i];
        }
        return bitmapPixels;
    }

    String yumDecodeBitMap(final Bitmap image) {
        String resultText = null;
        try {
            final int width = image.getWidth(), height = image.getHeight();
            final byte[] pixels = BitmapToArray(image);
            final LuminanceSource source = new PlanarYUVLuminanceSource(pixels, width, height, 0, 0, width, height, false);
            final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decodeWithState(bitmap);

            resultText = result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return resultText;
    }

    public static class Builder {

        private String mCharset = "UTF-8";

        /**
         * 设置文本编码格式
         *
         * @param charset 字符编码格式
         * @return Builder，用于链式调用
         */
        public Builder setCharset(String charset) {
            if (TextUtils.isEmpty(charset)) {
                throw new IllegalArgumentException("Illegal charset: " + charset);
            }
            mCharset = charset;
            return this;
        }

        public Decoder build() {
            return new Decoder(this);
        }
    }

}
