package com.baigu.dms.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.micky.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/4 11:04
 */
public class WXShare {

    public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
    public static final String EXTRA_RESULT = "result";

    private static final int THUMB_SIZE = 100;

    private Context context;
    private IWXAPI api;
    private OnResponseListener listener;
    private ResponseReceiver receiver;

    private static class WXShareHolder {
        private static final WXShare INSTANCE = new WXShare();
    }

    private WXShare() {
        context = BaseApplication.getContext();
        api = WXAPIFactory.createWXAPI(context, context.getString(R.string.wx_appid));
    }

    public static final WXShare getInstance() {
        return WXShare.WXShareHolder.INSTANCE;
    }

    public WXShare register() {
        // 微信分享
        api.registerApp(context.getString(R.string.wx_appid));
        receiver = new ResponseReceiver();
        IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
        context.registerReceiver(receiver, filter);
        return this;
    }

    public void unregister() {
        try {
            api.unregisterApp();
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WXShare shareText(String title, String content, String url, boolean toCircle) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        if (!TextUtils.isEmpty(url)) {
            webpageObject.webpageUrl = url;
        }

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpageObject;
        if (!TextUtils.isEmpty(title)) {
            msg.title = title;
        }
        if (!TextUtils.isEmpty(title)) {
            msg.description = content;
        }
        if (!TextUtils.isEmpty(url)) {
            msg.description = content;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = toCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        boolean result = api.sendReq(req);
        Logger.d("text shared: " + result);
        return this;
    }

    public void sendLocalImage(String path, boolean toCircle) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap bmp = getSmallBitmap(path);
        if(bmp == null){
            return;
        }
        msg.thumbData = bmpToByteArray(bmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = toCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        double rate = ((double) options.outWidth) / ((double) options.outHeight);
        options.inSampleSize = calculateInSampleSize(options, THUMB_SIZE, (int) (THUMB_SIZE *
                rate));
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    public IWXAPI getApi() {
        return api;
    }

    public void setListener(OnResponseListener listener) {
        this.listener = listener;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Response response = intent.getParcelableExtra(EXTRA_RESULT);
            Logger.d("type: " + response.getType());
            Logger.d("errCode: " + response.errCode);
            String result;
            if (listener != null) {
                if (response.errCode == BaseResp.ErrCode.ERR_OK) {
                    listener.onSuccess();
                } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    listener.onCancel();
                } else {
                    switch (response.errCode) {
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                            result = "发送被拒绝";
                            break;
                        case BaseResp.ErrCode.ERR_UNSUPPORT:
                            result = "不支持错误";
                            break;
                        default:
                            result = "发送返回";
                            break;
                    }
                    listener.onFail(result);
                }
            }
        }
    }

    public static class Response extends BaseResp implements Parcelable {

        public int errCode;
        public String errStr;
        public String transaction;
        public String openId;

        private int type;
        private boolean checkResult;

        public Response(BaseResp baseResp) {
            errCode = baseResp.errCode;
            errStr = baseResp.errStr;
            transaction = baseResp.transaction;
            openId = baseResp.openId;
            type = baseResp.getType();
            checkResult = baseResp.checkArgs();
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public boolean checkArgs() {
            return checkResult;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.errCode);
            dest.writeString(this.errStr);
            dest.writeString(this.transaction);
            dest.writeString(this.openId);
            dest.writeInt(this.type);
            dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
        }

        protected Response(Parcel in) {
            this.errCode = in.readInt();
            this.errStr = in.readString();
            this.transaction = in.readString();
            this.openId = in.readString();
            this.type = in.readInt();
            this.checkResult = in.readByte() != 0;
        }

        public static final Creator<Response> CREATOR = new Creator<Response>() {
            @Override
            public Response createFromParcel(Parcel source) {
                return new Response(source);
            }

            @Override
            public Response[] newArray(int size) {
                return new Response[size];
            }
        };
    }
}