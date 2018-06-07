package com.baigu.dms.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.GoodsSpecificationAdapter;
import com.baigu.dms.adapter.SkuDialogAdapter;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.GoodsAddPopView;
import com.baigu.dms.common.view.ObservableScrollView;
import com.baigu.dms.common.view.SharePopView;
import com.baigu.dms.common.view.SkuDialog;
import com.baigu.dms.common.view.SpaceItemDecoration;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.BuyNum;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.ShopPictrue;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.fragment.ShopFragment;
import com.baigu.dms.presenter.BuyNumPresenter;
import com.baigu.dms.presenter.GoodsDetailPresenter;
import com.baigu.dms.presenter.impl.BuyNumPresenterimpl;
import com.baigu.dms.presenter.impl.GoodsDetailPresenterImpl;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 添加（确认）订单
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class GoodsDetailActivity extends BaseActivity implements GoodsDetailPresenter.GoodsDetailView, View.OnClickListener,BuyNumPresenter.BuyNumView {

    public static final int ADVERT_INTERVAL_TIME = 6000;

    private Banner mBanner;
    private LinearLayout mRlToollbar;
    private TextView mTvGoodsName;
    private TextView mTvGoodsPrice;
    private TextView mGoodsStock;
    private TextView mGoodsWeight;
    private TextView mGoodsBuyNumber;
    private WebView mWebView;
    private ImageView mIvBackBg;
    private ImageView mIvShareBg;
    private TextView mTvTitle;
    private RecyclerView goods_specification;
    private GoodsAddPopView mGoodsAddPopView;
    private SkuDialogAdapter goodsSpecificationAdapter;
    private SharePopView mSharePopView;
    private int mToolbarBgAlpha;
    private List<String> mGoodsDetailImageList = new ArrayList<>();
    private Map<String, Integer> mapNumber; //记录选择中position及数量
    private GoodsDetailPresenter mGoodsDetailPresenter;
    private BuyNumPresenter buyNumPresenter;
    private Goods mGoods;
    private int defaultSku;
    private ConfirmDialog mConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        initView();
        mTvTitle.setText(R.string.goods_detail);
        String goodsId = getIntent().getStringExtra("goodsId");

        if (goodsId == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            finish();
            return;
        }
        mGoodsDetailPresenter = new GoodsDetailPresenterImpl(this, this);
        buyNumPresenter = new BuyNumPresenterimpl(this,this);
        mGoodsDetailPresenter.loadGoodsDetail(goodsId);
    }

    private void initView() {

        mRlToollbar = findView(R.id.ll_toolbar);
        mRlToollbar.getBackground().mutate().setAlpha(0);
        findViewById(R.id.fl_back).setOnClickListener(this);
        findViewById(R.id.fl_share).setOnClickListener(this);
        mIvBackBg = findView(R.id.iv_back_bg);
        mIvShareBg = findView(R.id.iv_share_bg);
        mTvTitle = findView(R.id.tv_title);
        //待修改
        goods_specification = (RecyclerView) findViewById(R.id.gv_good_specification);


        mBanner = (Banner) findViewById(R.id.banner);

        DisplayMetrics dm = ViewUtils.getScreenInfo(this);
        mBanner.getLayoutParams().height = (int) (dm.widthPixels * 0.8);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(ADVERT_INTERVAL_TIME);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mTvGoodsName = findView(R.id.tv_goods_name);
        mTvGoodsPrice = findView(R.id.tv_goods_price);
        mGoodsStock = findView(R.id.tv_goods_stock);
        mGoodsBuyNumber = findView(R.id.tv_goods_buyNumber);
        mGoodsWeight = findView(R.id.tv_goods_weight);
        findViewById(R.id.tv_add_cart).setOnClickListener(this);
        findViewById(R.id.tv_submit_order).setOnClickListener(this);

        mWebView = findView(R.id.webView);
//        ViewUtils.setupWebViewSettings(mWebView.getSettings());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        if (Constants.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView();
        }
        ObservableScrollView scrollView = findView(R.id.scrollView);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (mRlToollbar == null) return;
                float height = (int) (ViewUtils.getScreenInfo(GoodsDetailActivity.this).widthPixels * 0.8);  //获取图片的高度
                if (y < height) {
                    mToolbarBgAlpha = Float.valueOf(y / height * 255).intValue();    //i 有可能小于 0
                } else {
                    mToolbarBgAlpha = 255;
                }
                updateAlpha();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAlpha();
    }

    private void updateAlpha() {
        mRlToollbar.getBackground().mutate().setAlpha(Math.max(mToolbarBgAlpha, 0));   // 0~255 透明度
        mIvBackBg.getBackground().mutate().setAlpha(Math.max(255 - mToolbarBgAlpha, 0));
        mIvShareBg.getBackground().mutate().setAlpha(Math.max(255 - mToolbarBgAlpha, 0));
        mTvTitle.setAlpha(Math.max(mToolbarBgAlpha, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_goods_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_share) {

        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(11)
    private void fixWebView() {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
    }

    public void setImageData(List<String> imageList) {
        if (imageList == null || imageList.size() <= 0) {
            mGoodsDetailImageList.clear();
        } else {
            mGoodsDetailImageList.clear();
            mGoodsDetailImageList.addAll(imageList);
        }
        mBanner.setImages(mGoodsDetailImageList);
        mBanner.start();

    }

    @Override
    public void onLoadGoodsDetail(final Goods goods) {
        mGoods = goods;
        changGoodsSku();

        if (mGoods != null) {
            Goods goodsTmp = ShopCart.getGoods(goods.getIds());
            if (goodsTmp != null) {
                mGoods.setBuyNum(goodsTmp.getBuyNum());
            }
            mTvTitle.setText(goods.getGoodsname());
            List<String> urlList = new ArrayList<>();
            if (!TextUtils.isEmpty(goods.getGoodsdetailpath())) {
                if (goods.getGoodsdetailpath().contains(",")) {
                    String[] urlArr = goods.getGoodsdetailpath().split(",");
                    for (String url : urlArr) {
                        urlList.add(url);
                    }
                } else {
                    urlList.add(goods.getGoodsdetailpath());
                }
            }
            if (urlList.size() == 0) {
                List<ShopPictrue> pics = mGoods.getPics();
                for (ShopPictrue shopPictrue: pics) {
                    urlList.add(shopPictrue.getPicUrl());
                }

            }
            setImageData(urlList);
            mTvGoodsName.setText(mGoods.getGoodsname());
            final char symbol = 165;

            /**
             * 2018-5-24  去掉多少money起  改为多少到多少价格
             */
            if (goods.getSkus().get(defaultSku).getMinPrice() == null || goods.getSkus().get(defaultSku).getMinPrice() == null){
                mTvGoodsPrice.setText(String.valueOf(String.valueOf(symbol) + goods.getSkus().get(defaultSku).getUniformprice()));
            }else {
                mTvGoodsPrice.setText(String.valueOf(String.valueOf(symbol) + goods.getSkus().get(defaultSku).getMinPrice() + "-" + goods.getSkus().get(defaultSku).getMaxPrice()));
            }

            mGoodsStock.setText(getString(R.string.stock_label, String.valueOf(mGoods.getSkus().get(defaultSku).getStocknum())));
            mGoodsStock.setVisibility(goods.getIsshow() == Goods.StockShowType.SHOW ? View.VISIBLE : View.GONE);
            mGoodsBuyNumber.setText(getString(R.string.goods_buy_number, String.valueOf(mGoods.getSkus().get(defaultSku).getBuyNum()))+"笔");
            mGoodsWeight.setText(getString(R.string.weight_label, StringUtils.getWeightString(mGoods.getSkus().get(defaultSku).getGoodsweight())));
            mWebView.loadData(StringUtils.decodeHtmlString(mGoods.getGoodsdetail()), "text/html; charset=UTF-8", null);

            goodsSpecificationAdapter = new SkuDialogAdapter(this);
            goodsSpecificationAdapter.setData(mGoods.getSkus());
            goodsSpecificationAdapter.setSelsed(defaultSku);
            goodsSpecificationAdapter.setMapNumber(mapNumber);
            goodsSpecificationAdapter.setListener(new OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    /**
                     * 2018-5-24  去掉多少money起  改为多少到多少价格
                     */
                    if (goods.getSkus().get(position).getMinPrice() == null || goods.getSkus().get(position).getMinPrice() == null){
                        mTvGoodsPrice.setText(String.valueOf(String.valueOf(symbol) + goods.getSkus().get(position).getUniformprice()));
                    }else {
                        mTvGoodsPrice.setText(String.valueOf(String.valueOf(symbol) + goods.getSkus().get(position).getMinPrice() + "-" + goods.getSkus().get(position).getMaxPrice()));
                    }
                    mGoodsStock.setText(getString(R.string.stock_label, String.valueOf(mGoods.getSkus().get(position).getStocknum())));
                    mGoodsStock.setVisibility(mGoods.getIsshow() == Goods.StockShowType.SHOW ? View.VISIBLE : View.GONE);
                    mGoodsBuyNumber.setText(getString(R.string.goods_buy_number, String.valueOf(mGoods.getSkus().get(position).getBuyNum()))+"笔");
                    mGoodsWeight.setText(getString(R.string.weight_label, StringUtils.getWeightString(mGoods.getSkus().get(position).getGoodsweight())));
                }
            });
            GridLayoutManager manager = new GridLayoutManager(this, 4);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    String attr = goods.getSkus().get(position).getSkuAttr();
                    int length = 1;
                    if (attr.length() <= 5) {
                        length = 1;
                    } else if (attr.length() > 5 && attr.length() < 10) {
                        length = 2;
                    } else if (attr.length() >= 10) {
                        length = 4;
                    }
                    return length;
                }
            });
            goods_specification.setLayoutManager(manager);
            goods_specification.setAdapter(goodsSpecificationAdapter);

            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent intent =new Intent(GoodsDetailActivity.this,ShowImageActivity.class);
                    intent.putExtra("position",position);
                    intent.putStringArrayListExtra("data", (ArrayList<String>) mGoodsDetailImageList);
                    startActivity(intent);
                }
            });
        } else {
            ViewUtils.showToastError(R.string.failed_load_data);
            finish();
        }
    }

    private void changGoodsSku() {
//        sortSku();
        for (Sku sku : mGoods.getSkus()) {
            try {
                StringBuilder tv_sku = new StringBuilder();
                JSONArray array = new JSONArray(sku.getSkuAttr());
                for (int j = 0; j < array.length(); j++) {
                    if (j > 0) {
                        tv_sku.append("+");
                    }
                    tv_sku.append(array.getJSONObject(j).getString("value"));
                }
                sku.setSkuAttr(tv_sku.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setNumberMap();
    }

    //排序
    private void sortSku() {
        List<Sku> skuList = mGoods.getSkus();
        if (skuList != null && skuList.size() > 0) {
            for (int i = 0; i < skuList.size(); i++) {
                if (skuList.get(i).isDefault()) {
                    defaultSku = i;
                }
                for (int j = skuList.size() - 1; j > i; j--) {
                    Sku skuj = skuList.get(j);
                    Sku skui = skuList.get(i);
                    if (skuList.get(i).getSkuAttr().length() >= skuList.get(j).getSkuAttr().length()) {
                        skuList.set(i, skuj);
                        skuList.set(j, skui);
                    }
                }
            }
        }
    }

    private void setNumberMap() {
        mapNumber = new HashMap<>();

        List<Goods> goodsList = ShopCart.getGoodsListSelected();
        if (goodsList.size() > 0) {
            for (Goods good : goodsList) {
                if (good.getIds().equals(mGoods.getIds())) {
                    for (int i = 0; i < good.getSkus().size(); i++) {
                        mapNumber.put(good.getSkus().get(i).getSkuId(), good.getSkus().get(i).getNumber());
                    }
                }
            }
        } else {
            for (int i = 0; i < mGoods.getSkus().size(); i++) {
                mapNumber.put(mGoods.getSkus().get(i).getSkuId(), 0);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoodsDetailPresenter != null) {
            mGoodsDetailPresenter.onDestroy();
        }
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
            case R.id.fl_share:
                if (mGoods == null) return;
                if (mSharePopView == null) {
                    mSharePopView = new SharePopView(this);
                }
                if (mGoodsAddPopView != null && mGoodsAddPopView.isShowing()) {
                    mGoodsAddPopView.dismiss();
                }
                mSharePopView.showAtLocation(this.findViewById(R.id.rl_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_add_cart:
                SkuDialog dialog = new SkuDialog(this);
                dialog.setData(mGoods);
                dialog.setCancelListener(new SkuDialog.CancelListener() {
                    @Override
                    public void UnmberUpData(List<Sku> skus) {
                        setNumberMap();
                        goodsSpecificationAdapter.setMapNumber(mapNumber);
                        goodsSpecificationAdapter.notifyDataSetChanged();
                        finish();
                    }
                });
                dialog.show();
                break;
            case R.id.tv_submit_order:
                if (mGoods == null) return;
                SkuDialog mdialog = new SkuDialog(this);
                mdialog.setData(mGoods);
                mdialog.setCancelListener(new SkuDialog.CancelListener() {
                    @Override
                    public void UnmberUpData(List<Sku> skus) {
                        if (ShopCart.getGoodsListSelected().size() <= 0 || !ShopCart.hasContains(mGoods)) {
                            ViewUtils.showToastInfo(R.string.buynumber_zero);
                            setNumberMap();
                            goodsSpecificationAdapter.setMapNumber(mapNumber);
                            goodsSpecificationAdapter.notifyDataSetChanged();
                            return;
                        } else {
                            List<Goods> goodsList = new ArrayList<>();
                            goodsList.add(mGoods);
                            buyNumPresenter.buyNum(goodsList);
                        }

                    }
                });
                mdialog.show();
                break;
        }
    }

    private void addGoods(boolean addCart) {

        if (mConfirmDialog == null) {
            mConfirmDialog = new ConfirmDialog(this, "");
            mConfirmDialog.setHideCancel(true);
            mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                @Override
                public void onOKClick(View v) {
                    mConfirmDialog.dismiss();
                }
            });
        }
        if (mGoodsAddPopView == null) {
            mGoodsAddPopView = new GoodsAddPopView(this);
        }

        if (mSharePopView != null && mSharePopView.isShowing()) {
            mSharePopView.dismiss();
        }
        mGoodsAddPopView.initData(addCart, mGoods);
        mGoodsAddPopView.showAtLocation(this.findViewById(R.id.rl_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void isBuy(BuyNum result) {
        if (result == null){
            ViewUtils.showToastError(R.string.failed_load_data);
        }else {
            if (result.getCode() == 1){
                Intent intent = new Intent(GoodsDetailActivity.this, AddOrderActivity.class);
                List<Goods> goodsList = new ArrayList<>();
                goodsList.add(mGoods);
                intent.putParcelableArrayListExtra("goodsList", (ArrayList<? extends Parcelable>) goodsList);
                startActivity(intent);
                finish();
            }else {
                ViewUtils.showToastError(result.getResult());
            }
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(GoodsDetailActivity.this).load(path).placeholder(R.mipmap.place_holder).centerCrop().into(imageView);
        }
    }

}
