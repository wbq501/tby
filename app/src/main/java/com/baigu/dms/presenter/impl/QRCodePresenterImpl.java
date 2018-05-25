package com.baigu.dms.presenter.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.view.qrcode.core.Decoder;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.QRCodePresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class QRCodePresenterImpl extends BasePresenterImpl implements QRCodePresenter {
    private QRCodeView mQRCodeView;

    public QRCodePresenterImpl(BaseActivity activity, QRCodeView qrCodeView) {
        super(activity);
        mQRCodeView = qrCodeView;
    }

    @Override
    public void decode(String path) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.qrcode_parsing);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    Bitmap bitmap = getImageWidthHeight(params[0]);
                    result = new Decoder.Builder().build().decode(bitmap);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (mQRCodeView != null) {
                    mQRCodeView.onDecode(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mQRCodeView != null) {
                    mQRCodeView.onDecode("");
                }
            }
        }.execute(path));
    }

    private Bitmap getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        int maxBorder = Math.max(options.outWidth, options.outHeight);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        if (maxBorder > 1080) {//如图片过大，进行缩放
            int scoal = maxBorder / 1080;
            scoal++;
            options.inSampleSize = scoal;
        }
        return BitmapFactory.decodeFile(path, options);
    }
}
