package com.baigu.dms.common.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BuyGoodsType;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.micky.logger.Logger;

/**
 * @Description 数量增减View
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017-08-31 下午 4:44
 */
public class NumberView extends FrameLayout implements View.OnClickListener {
    private TextView mTvNum;
    private ImageView mIvAdd;
    private ImageView mIvSub;

    private int mCurrNum = 0; //购买数量
    private int mMaxNum = 0; //商品库存
    private int mMinNum = 0; //最小值

    private Sku sku;//选择的商品类型

    private OnNumChangeListener mOnNumChangeListener;

    public NumberView(Context context) {
        super(context);
        init();
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.mOnNumChangeListener = onNumChangeListener;
    }

    public void setMinNum(int minNum) {
        mMinNum = minNum;
        setCurrNum(minNum);
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public void setCurrNum(int num) {
        mCurrNum = num;
        mTvNum.setText(String.valueOf(mCurrNum));
    }

    public int getmCurrNum() {
        return mCurrNum;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_number_view, this);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mIvAdd.setOnClickListener(this);
        mIvSub = (ImageView) findViewById(R.id.iv_sub);
        mIvSub.setOnClickListener(this);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                mCurrNum = Integer.valueOf(s.toString());
                if (mCurrNum <= 0) {
//                    mIvSub.setImageBitmap(null);
//                    mTvNum.setText(0+"");
                } else {
                    mIvSub.setImageResource(R.mipmap.num_sub_pressed);
                    mTvNum.setTextColor(getResources().getColor(R.color.main_text));
                }
            }
        });
        mTvNum.setText(String.valueOf(mCurrNum));
    }

    private boolean isMore = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                if (mOnNumChangeListener != null && !mOnNumChangeListener.onAbleChanged(mCurrNum)) {
                    return;
                }

                if (mCurrNum < mMaxNum) {
                    if (!BuyGoodsType.isBuy(getSku(),false,false)){
                        ViewUtils.showToastError(R.string.buy_type);
                        return;
                    }
                    mCurrNum++;
                }else if (mCurrNum == mMaxNum){
                    isMore = true;
                }
                mTvNum.setText(String.valueOf(mCurrNum));
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onNumChanged(mCurrNum,true,isMore);
                }
                break;
            case R.id.iv_sub:
                if (mOnNumChangeListener != null && !mOnNumChangeListener.onAbleChanged(mCurrNum)) {
                    return;
                }
                if (mCurrNum > mMinNum) {
                    isMore = false;
                    mCurrNum--;
                }

                if (mCurrNum == 0){
                    BuyGoodsType.isBuy(getSku(),true,false);
                }

                mTvNum.setText(String.valueOf(mCurrNum));
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onNumChanged(mCurrNum,false,isMore);
                }
                break;
            default:
                break;
        }
    }

    public interface OnNumChangeListener {
        boolean onAbleChanged(int currNum);
        void onNumChanged(int amount,boolean isAdd,boolean isMore);
    }
}
