package com.baigu.dms.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.baigu.dms.activity.BaseActivity;
import com.micky.logger.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/4 11:02
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("WXEntryActivity");
        api = WXShare.getInstance().getApi();

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            if (!api.handleIntent(getIntent(), this)) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Logger.i("onNewIntent");
        setIntent(intent);
        if (!api.handleIntent(intent, this)) {
            finish();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Intent intent = new Intent(WXShare.ACTION_SHARE_RESPONSE);
        intent.putExtra(WXShare.EXTRA_RESULT, new WXShare.Response(baseResp));
        sendBroadcast(intent);
        finish();
    }

}