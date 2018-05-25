package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.AddOrderActivity;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/26 21:10
 */
public class GoodsAddPopView extends PopupWindow implements OnClickListener {

    private FrameLayout mFlContainer;
    private View mView;
    private TextView mTvName;
    private TextView mTvStock;
    private TextView mTvTotalPrice;
    private ImageView mIvGoods;
    private NumberView mNumberView;
    private Context mContext;
    private boolean mAddCart;
    private ConfirmDialog mConfirmDialog;

    private Goods mGoods;

    public GoodsAddPopView(Activity context) {
        super(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.view_goods_add_pop, null);
        mFlContainer = (FrameLayout) mView.findViewById(R.id.fl_container);
        this.setContentView(mView);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.GoodsAddPopWindow);
        ColorDrawable dw = new ColorDrawable(0xA0000000);
        mView.setBackground(dw);
        mView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mFlContainer.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        mTvName = (TextView) mView.findViewById(R.id.tv_name);
        mTvTotalPrice = (TextView) mView.findViewById(R.id.tv_total_price);
        mTvStock = (TextView) mView.findViewById(R.id.tv_stock);
        mIvGoods = (ImageView) mView.findViewById(R.id.iv_goods);
        mNumberView = (NumberView) mView.findViewById(R.id.number_view);
        mNumberView.setMinNum(1);
        mNumberView.setOnNumChangeListener(new NumberView.OnNumChangeListener() {

            @Override
            public boolean onAbleChanged(int mCurrNum) {
                if (mConfirmDialog == null) {
                    mConfirmDialog = new ConfirmDialog((Activity) mContext, "");
                    mConfirmDialog.setHideCancel(true);
                    mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                        @Override
                        public void onOKClick(View v) {
                            mConfirmDialog.dismiss();
                        }
                    });
                }
                if (mAddCart) {
                    return ShopCart.validGoodsLimit(mContext, mGoods, mConfirmDialog) && ShopCart.validGoodsRule(mContext, mGoods, mConfirmDialog);
                } else {
                    return ShopCart.validGoodsLimit(mContext, mGoods, mConfirmDialog);
                }
            }

            @Override
            public void onNumChanged(int amount) {
                updatePriceLabel(amount);

            }
        });
        mView.findViewById(R.id.ll_ok).setOnClickListener(this);
    }

    private void updatePriceLabel(int amount) {
        mGoods.setBuyNum(amount);
        double totalPrice = amount * mGoods.getUniformprice();
        char symbol = 165;
        String priceStr = String.valueOf(symbol) + totalPrice;
        mTvTotalPrice.setText(mContext.getString(R.string.total_price4, priceStr));
    }

    public void initData(boolean addCart, Goods goods) {

        mAddCart = addCart;
        mGoods = goods;
        int buyNum = goods.getBuyNum() <= 0 ? 1 : goods.getBuyNum();
        mGoods.setBuyNum(buyNum);
        mTvName.setText(goods.getGoodsname());
        updatePriceLabel(buyNum);
        mTvStock.setText(mContext.getString(R.string.stock_label, String.valueOf(goods.getStocknum())));
        mTvStock.setVisibility(goods.getIsshow() == Goods.StockShowType.SHOW ? View.VISIBLE : View.GONE);
        mNumberView.setCurrNum(goods.getBuyNum());
        mNumberView.setMaxNum(goods.getStocknum());
        Glide.with(mContext).load(goods.getCoverpath()).centerCrop().placeholder(R.mipmap.place_holder).into(mIvGoods);
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.ll_ok:
                if (mAddCart) {
                    if (!(ShopCart.validGoodsLimitGt(mContext, mGoods, mConfirmDialog) && ShopCart.validGoodsRule(mContext, mGoods, mConfirmDialog))) {
                        return;
                    }
                    ShopCart.addGoods(mGoods);
                    ShopCart.notifyDataChanged();
                    ((Activity) mContext).finish();
                } else {
                    if (!ShopCart.validGoodsLimitGt(mContext, mGoods, mConfirmDialog)) {
                        return;
                    }
                    Intent intent = new Intent(mContext, AddOrderActivity.class);
                    ArrayList<Goods> goodsList = new ArrayList<>();
                    goodsList.add(mGoods);
                    intent.putExtra("goodsList", goodsList);
                    mContext.startActivity(intent);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }
}