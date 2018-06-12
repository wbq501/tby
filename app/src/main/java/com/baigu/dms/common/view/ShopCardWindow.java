package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.AddOrderActivity;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.adapter.ShopWindowAdapter;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.BuyNum;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BuyNumPresenter;
import com.baigu.dms.presenter.impl.BuyNumPresenterimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class ShopCardWindow extends PopupWindow implements View.OnClickListener,BuyNumPresenter.BuyNumView {

    private Context context;
    private ListView shopCard;
    private LinearLayout cleanShopCard;
    private ImageView close;
    private TextView price;
    private ShopWindowAdapter adapter;
    private List<Goods> mGoodsList;

    private BuyNumPresenter buyNumPresenter;

    public ShopCardWindow(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_shopcard, null);
        setContentView(view);
        WindowManager mg = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setHeight(mg.getDefaultDisplay().getHeight() * 2 / 3);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable();
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.ShopCardWinow);
        setFocusable(true);
        adapter = new ShopWindowAdapter(context);
        buyNumPresenter = new BuyNumPresenterimpl((Activity) context,this);
        initView(view);
    }

    private void initView(View view) {
        shopCard = view.findViewById(R.id.lv_shopcard);
        price = view.findViewById(R.id.tv_total_price);
        cleanShopCard = view.findViewById(R.id.ll_shopcard_clean);
        close = view.findViewById(R.id.iv_shopcard_close);

        price.setText("￥" + ShopCart.getCount());
        adapter.setChangeListener(new ShopWindowAdapter.TotalChangeListener() {
            @Override
            public void TotalChange() {
                price.setText("￥" + ShopCart.getCount());
            }
        });
        shopCard.setAdapter(adapter);

        close.setOnClickListener(this);
        cleanShopCard.setOnClickListener(this);

        view.findViewById(R.id.ll_shopcard_order).setOnClickListener(this);

        setShopCardData();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shopcard_close:
                dismiss();
                break;
            case R.id.ll_shopcard_clean:
                ShopCart.clearCart();
                dismiss();
                break;
            case R.id.ll_shopcard_order:
                buyNumPresenter.buyNum(ShopCart.getGoodsListSelected());
                break;
        }
    }

    private void setShopCardData() {
        mGoodsList = new ArrayList<>();
        List<Goods> goodsList = ShopCart.getGoodsListSelected();
        for (Goods goods : goodsList) {
            if (goods.getSkus().size() > 1) {
                for (Sku sku : goods.getSkus()) {
                    if (sku.getNumber() > 0) {
                        Goods mgoods = new Goods();
                        List<Sku> mskus = new ArrayList<Sku>();
                        mskus.add(sku);
                        mgoods.setStocknum(sku.getStocknum());
                        mgoods.setIds(goods.getIds());
                        mgoods.setSupercoverpath(goods.getSupercoverpath());
                        mgoods.setCoverpath(goods.getCoverpath());
                        mgoods.setBuyNum(sku.getNumber());
                        mgoods.setUniformprice(sku.getUniformprice());
                        mgoods.setMarketprice(sku.getMarketprice());
                        mgoods.setGoodsname(goods.getGoodsname());
                        mgoods.setSkus(mskus);
                        mgoods.setCategory(goods.getCategory());
                        mGoodsList.add(mgoods);
                    }
                }

            } else {
                Goods mgoods = new Goods();
                List<Sku> mskus = new ArrayList<Sku>();
                if (goods.getSkus().get(0).getNumber() > 0){
                    mskus.add(goods.getSkus().get(0));
                    mgoods.setIds(goods.getIds());
                    mgoods.setSupercoverpath(goods.getSupercoverpath());
                    mgoods.setCoverpath(goods.getCoverpath());
                    mgoods.setStocknum(goods.getSkus().get(0).getStocknum());
                    mgoods.setBuyNum(goods.getSkus().get(0).getNumber());
                    mgoods.setUniformprice(goods.getSkus().get(0).getUniformprice());
                    mgoods.setMarketprice(goods.getSkus().get(0).getMarketprice());
                    mgoods.setGoodsname(goods.getGoodsname());
                    mgoods.setSkus(mskus);
                    mgoods.setCategory(goods.getCategory());
                    mGoodsList.add(mgoods);
                }
            }

        }
        adapter.setData(mGoodsList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void isBuy(BuyNum result) {
        if (result == null){
            ViewUtils.showToastError(R.string.failed_load_data);
        }else {
            if (result.getCode() == 1){
                Intent intent = new Intent();
                intent.setClass(context, AddOrderActivity.class);
                intent.putParcelableArrayListExtra("goodsList", ShopCart.getGoodsListSelected());
                context.startActivity(intent);
                dismiss();
            }else {
                ViewUtils.showToastError(result.getResult());
            }
        }
    }
}
