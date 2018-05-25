package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.NumberView;
import com.baigu.dms.common.view.SkuDialog;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.ShopPictrue;
import com.baigu.dms.domain.model.Sku;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsAdapter extends BaseRVAdapter<Goods> {
    public Activity mActivity;
    private ConfirmDialog mConfirmDialog;
    private OnGoodsAmountChangeListener mOnGoodsAmountChangeListener;
    private List<Integer> mTitleIntList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean mShouldScroll = false;
    private SkuDialog dialog;

    public GoodsAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setmTitleIntList(List<Integer> mTitleIntList) {
        this.mTitleIntList = mTitleIntList;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setOnGoodsAmountChangeListener(OnGoodsAmountChangeListener listener) {
        mOnGoodsAmountChangeListener = listener;

    }

    public void clearSelectGoods() {
        ShopCart.getGoodsListSelected().clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return new GoodsAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Goods goods = getItem(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final Context context = itemViewHolder.itemView.getContext();
        List<ShopPictrue> pics = goods.getPics();
        for (ShopPictrue shopPictrue : pics) {
            if (shopPictrue.getPosition() == 1) {
                Glide.with(context).load(shopPictrue.getPicUrl()).centerCrop().placeholder(R.mipmap.place_holder).into(itemViewHolder.ivGoods);
            }
        }

        itemViewHolder.tvGoodsName.setText(goods.getGoodsname());
        char symbol = 165;
        itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + String.valueOf(goods.getUniformprice()));
        //寻找最低价
        if (goods.getSkus().size() > 0) {
            double minPrice = goods.getSkus().get(0).getUniformprice();

            if (goods.getSkus().get(0).getMinPrice() == null || goods.getSkus().get(0).getMaxPrice() == null){
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + String.valueOf(minPrice));
            }else {
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol)+goods.getSkus().get(0).getMinPrice()+"-"+String.valueOf(symbol)+goods.getSkus().get(0).getMaxPrice());
            }

            /**
             * 2018-5-24  去掉多少money起  改为多少到多少价格 见上面
             */
            /**if (goods.getSkus().size() > 1) {
                for (Sku sku : goods.getSkus()) {
                    if (sku.getUniformprice() < minPrice) {
                        minPrice = sku.getUniformprice();
                    }
                }
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + minPrice + "起");
            } else {
                itemViewHolder.tvGoodsPrice.setText(String.valueOf(symbol) + String.valueOf(minPrice));
            }*/
        }

        for (Sku sku : goods.getSkus()) {
            if (sku.getSkuAttr().length() > 10) {
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
        }
//        itemViewHolder.tvGoodsWeight.setText("/" + StringUtils.getWeightString(goods.getGoodsweight()));
//        itemViewHolder.tvGoodsStock.setText(context.getString(R.string.stock_label, String.valueOf(goods.getStocknum())));
//        itemViewHolder.tvGoodsStock.setVisibility(goods.getIsshow() == Goods.StockShowType.SHOW ? View.VISIBLE : View.GONE);
//        itemViewHolder.numberView.setMaxNum(goods.getStocknum());

        if (goods.getSkus().size() > 0) {
            itemViewHolder.numberView.setMaxNum(goods.getSkus().get(0).getStocknum());
        }

        Goods cartGoods = ShopCart.getGoods(goods.getIds());


        if (cartGoods != null) {
            goods.setBuyNum(cartGoods.getBuyNum());
        }
        if (goods.getSkus().size() == 1) {
            itemViewHolder.numberView.setCurrNum(goods.getSkus().get(0).getNumber());
        }

        if (goods.getStocknum() > 0) {
            itemViewHolder.numberView.setVisibility(View.VISIBLE);
            itemViewHolder.tvGoodsSku.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.numberView.setVisibility(View.GONE);
            itemViewHolder.tvGoodsSku.setVisibility(View.GONE);
        }

        if (goods.getSkus().size() > 1) {
            itemViewHolder.tvGoodsSku.setVisibility(View.VISIBLE);
            itemViewHolder.numberView.setVisibility(View.GONE);
        } else {
            itemViewHolder.tvGoodsSku.setVisibility(View.GONE);
            itemViewHolder.numberView.setVisibility(View.VISIBLE);
        }
        itemViewHolder.numberView.setOnNumChangeListener(new OnGoodsNumChangeListener(goods));
        int countNumber = 0;
        List<Goods> goodsList = ShopCart.getGoodsListSelected();
        if (goodsList.size() > 0) {
            for (Goods mgoods : goodsList) {
                if (goods.getIds().equals(mgoods.getIds())) {
                    for (Sku s : mgoods.getSkus()) {
                        countNumber += s.getNumber();
                    }
                }
            }
        }
        if (countNumber > 0) {
            itemViewHolder.tvSkuNumber.setText(String.valueOf(countNumber));
            itemViewHolder.tvSkuNumber.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.tvSkuNumber.setVisibility(View.GONE);
        }

        itemViewHolder.tvGoodsSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new SkuDialog(context);
                dialog.setData(goods);
                dialog.setCancelListener(new SkuDialog.CancelListener() {
                    @Override
                    public void UnmberUpData(List<Sku> skus) {
//                        goods.setSkus(skus);
                        if (mOnGoodsAmountChangeListener != null) {
                            mOnGoodsAmountChangeListener.onAmountChanged(skus, position);
                        }
                        notifyDataSetChanged();
                    }
                });
                dialog.show();

            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGoods;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
//        TextView tvGoodsStock;
        NumberView numberView;
        //        TextView tvCategory;
        Button tvGoodsSku;
        TextView tvSkuNumber;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivGoods = (ImageView) itemView.findViewById(R.id.iv_goods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
//            tvGoodsWeight = (TextView) itemView.findViewById(R.id.tv_goods_weight);
//            tvGoodsStock = (TextView) itemView.findViewById(R.id.tv_goods_stock);
            numberView = (NumberView) itemView.findViewById(R.id.number_view);
//            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvGoodsSku = (Button) itemView.findViewById(R.id.tv_goods_sku);
            tvSkuNumber = (TextView) itemView.findViewById(R.id.tv_sku_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(GoodsAdapter.this, getLayoutPosition());
                }
            });
        }
    }

    class OnGoodsNumChangeListener implements NumberView.OnNumChangeListener {

        private Goods goods;

        public OnGoodsNumChangeListener(Goods goods) {
            this.goods = goods;
        }

        @Override
        public boolean onAbleChanged(int mCurrNum) {
            if (mConfirmDialog == null) {
                mConfirmDialog = new ConfirmDialog(mActivity, "");
                mConfirmDialog.setHideCancel(true);
                mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                    @Override
                    public void onOKClick(View v) {
                        mConfirmDialog.dismiss();
                    }
                });
            }
            return ShopCart.validGoodsLimit(mActivity, goods, mConfirmDialog) && ShopCart.validGoodsRule(mActivity, goods, mConfirmDialog);
        }

        @Override
        public void onNumChanged(int amount) {
            goods.getSkus().get(0).setNumber(amount);
            ShopCart.addGoods(goods);
            if (mOnGoodsAmountChangeListener != null) {
                mOnGoodsAmountChangeListener.onAmountChanged();
            }
        }
    }

    public interface OnGoodsAmountChangeListener {
        void onAmountChanged();

        void onAmountChanged(List<Sku> skus, int position);
    }

    public void setSelection(int pos) {
        if (pos < getDataList().size()) {
            moveToPosition(pos);
        }
    }


    private void moveToPosition(final int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int lastItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
        int pos = mTitleIntList.get(n);
        Log.i("test", pos + "--");
        mShouldScroll = false;
        mRecyclerView.setOnScrollListener(new RecyclerViewListener(n));
        //然后区分情况
        if (pos <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(pos);
        } else if (pos <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(pos - firstItem).getTop() - 50;
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时,调用scrollToPosition只会将该项滑动到屏幕上。需要再次滑动到顶部
            mRecyclerView.scrollToPosition(pos);
            //这里这个变量是用在RecyclerView滚动监听里面的
            mShouldScroll = true;
        }
    }

    /**
     * 滚动监听
     */
    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        private int n = 0;

        RecyclerViewListener(int n) {
            this.n = n;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动
            if (mShouldScroll) {
                mShouldScroll = false;
                moveToPosition(n);
            }
        }
    }
}
