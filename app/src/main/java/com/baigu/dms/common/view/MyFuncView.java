package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BlogActivity;
import com.baigu.dms.activity.CertificationStep1Activity;
import com.baigu.dms.activity.ChatActivity;
import com.baigu.dms.activity.GestureVerifyActivity;
import com.baigu.dms.activity.LogisticsWebActivity;
import com.baigu.dms.activity.MyRewardActivity;
import com.baigu.dms.activity.SettingActivity;
import com.baigu.dms.activity.WalletSecurityActivity;
import com.baigu.dms.activity.WebActivity;
import com.baigu.dms.activity.CouponActivity;
import com.baigu.dms.activity.CustomServiceActivity;
import com.baigu.dms.activity.AddressListActivity;
import com.baigu.dms.activity.MyInvitationActivity;
import com.baigu.dms.activity.MyInviteCodeActivity;
import com.baigu.dms.activity.ScoreShopActivity;
import com.baigu.dms.activity.WalletActivity;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.IMHelper;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.bumptech.glide.Glide;
import com.micky.logger.Logger;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class MyFuncView extends FrameLayout implements View.OnClickListener {

    private ImageView mIvAd;
    private Advert mAdvert;
    private TextView mTvMessageNum;

    public MyFuncView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyFuncView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyFuncView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_my_func, this);
        View view = findViewById(R.id.layout_main);
        view.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;

        mTvMessageNum = (TextView) findViewById(R.id.tv_message_num);
        mIvAd = (ImageView) findViewById(R.id.iv_ad);
        DisplayMetrics dm = ViewUtils.getScreenInfo(getContext());
        mIvAd.getLayoutParams().width = dm.widthPixels;
        mIvAd.getLayoutParams().height = (int) (dm.widthPixels * 0.3);
        mIvAd.setOnClickListener(this);

        findViewById(R.id.layout_my_reward).setOnClickListener(this);
        findViewById(R.id.layout_wallet).setOnClickListener(this);
        findViewById(R.id.layout_coupon).setOnClickListener(this);
        findViewById(R.id.layout_score_shop).setOnClickListener(this);
        findViewById(R.id.layout_my_invite_code).setOnClickListener(this);
        findViewById(R.id.layout_my_invitation).setOnClickListener(this);
        findViewById(R.id.layout_delivery_address).setOnClickListener(this);
        findViewById(R.id.layout_custom_service).setOnClickListener(this);
        findViewById(R.id.iv_ad).setOnClickListener(this);
    }

    public void setData(Advert advert) {
        if (advert == null) {
            return;
        }
        mAdvert = advert;
        if (mAdvert == null) {
            mIvAd.setVisibility(View.GONE);
        } else {
            mIvAd.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mAdvert.getAdvertis_img()).placeholder(R.mipmap.place_holder).into(mIvAd);
        }
    }

    public void setMessageNum(int num) {
        if (num <= 0) {
            mTvMessageNum.setVisibility(View.GONE);
        } else if (num >= 99) {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(num + "+");
        } else {
            mTvMessageNum.setVisibility(View.VISIBLE);
            mTvMessageNum.setText(String.valueOf(num));
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.layout_wallet:
                getContext().startActivity(new Intent(getContext(), WalletActivity.class));
                break;
            case R.id.layout_coupon:
                getContext().startActivity(new Intent(getContext(), CouponActivity.class));
                break;
            case R.id.layout_score_shop:
                getContext().startActivity(new Intent(getContext(), ScoreShopActivity.class));
                break;
            case R.id.layout_my_invite_code:
                getContext().startActivity(new Intent(getContext(), MyInviteCodeActivity.class));
                break;
            case R.id.layout_my_invitation:
                getContext().startActivity(new Intent(getContext(), MyInvitationActivity.class));
                break;
            case R.id.layout_delivery_address:
                getContext().startActivity(new Intent(getContext(), AddressListActivity.class));
                break;
            case R.id.layout_custom_service:
                getContext().startActivity(new Intent(getContext(), ChatActivity.class));
                break;
            case R.id.layout_my_reward:
                Intent intentreward = new Intent(getContext(), MyRewardActivity.class);
                getContext().startActivity(intentreward);
                /*final LoadingDialog loadingDialog = new LoadingDialog((Activity) getContext());
                loadingDialog.show();
                ServiceManager.createGsonService(UserService.class)
                        .getReward()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse<String>>() {
                            @Override
                            public void accept(BaseResponse<String> response) throws Exception {
                                loadingDialog.hide();
                                if (response != null && BaseResponse.SUCCESS.equals(response.getStatus()) && !TextUtils.isEmpty(response.getData())) {
                                    Intent intent = new Intent(getContext(), WebActivity.class);
                                    intent.putExtra("title", getContext().getString(R.string.my_reward));
                                    String url = response.getData();
                                    if (url.contains("?")) {
                                        url += "&token=" + SPUtils.getObject("token","") + "&phone=" + UserCache.getInstance().getUser().getCellphone();
                                    } else {
                                        url += "?token=" + SPUtils.getObject("token","") + "&phone=" + UserCache.getInstance().getUser().getCellphone();
                                    }
                                    intent.putExtra("url", url);
                                    getContext().startActivity(intent);
                                } else {
                                    ViewUtils.showToastError(R.string.failed_get_reward);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                loadingDialog.hide();
                                ViewUtils.showToastError(R.string.failed_get_reward);
                            }
                        });*/

                break;
            case R.id.iv_ad:
                if (mAdvert != null) {
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("title", mAdvert.getAdvertis_title());
                    if (TextUtils.isEmpty(mAdvert.getAdvertis_content())) {
                        return;
                    }
                    intent.putExtra("content", mAdvert.getAdvertis_content());
                    getContext().startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

}
