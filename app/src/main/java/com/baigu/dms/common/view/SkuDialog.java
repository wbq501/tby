package com.baigu.dms.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.SkuDialogAdapter;
import com.baigu.dms.common.utils.BuyGoodsType;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class SkuDialog extends Dialog {

    private TextView tvMoney;
    private TextView tvSeletOK;
    private TextView tvTitle;
    private NumberView number;
    private RecyclerView sku;
    private Goods goods;
    private SkuDialogAdapter adapter;
    private Map<String, Integer> mapNumber; //记录选择中position及数量
    private CancelListener cancelListener;

    boolean isshow = true;

    public void setData(Goods goods) {
        sortSku(goods);
        this.goods = goods;

    }

    private void sortSku(Goods goods) {
        List<Sku> skuList = goods.getSkus();
        for (int i = 0; i < skuList.size(); i++) {
            for (int j = skuList.size() - 1; j > i; j--) {
                Sku skuj = skuList.get(j);
                Sku skui = skuList.get(i);
                if (skuList.get(i).getSkuAttr().length() > skuList.get(j).getSkuAttr().length()) {
                    skuList.set(i, skuj);
                    skuList.set(j, skui);
                }
            }
        }
    }

    public void setCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setMapNumber(Map<String, Integer> mapNumber) {
        this.mapNumber = mapNumber;
    }

    public SkuDialog(@NonNull Context context) {
        super(context);
    }

    public SkuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SkuDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_sku_dialog);
        initView();
    }

    private void initView() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        tvMoney = (TextView) findViewById(R.id.tv_goods_price);
        tvSeletOK = (TextView) findViewById(R.id.tv_goods_confirm);
        number = (NumberView) findViewById(R.id.number_view);
        tvTitle = (TextView) findViewById(R.id.tv_sku_title);
        sku = (RecyclerView) findViewById(R.id.rv_sku);
        getNumberMap();

        /**
         * 2018.5.24
         * 购买最大数量不超过库存
         */
        number.setMaxNum(buynum(goods.getSkus().get(0).getStocknum(),goods.getSkus().get(0).getMaxCount()));
//        number.setMaxNum(goods.getSkus().get(0).getStocknum());

        number.setCurrNum(mapNumber.get(goods.getSkus().get(0).getSkuId()));
        number.setSku(goods.getSkus().get(0));
        tvTitle.setText(goods.getGoodsname());

        tvSeletOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopCart.addGoods(goods);
                cancel();
            }
        });

        number.setOnNumChangeListener(new NumberView.OnNumChangeListener() {
            @Override
            public boolean onAbleChanged(int currNum) {
                return true;
            }

            @Override
            public void onNumChanged(int amount) {
                //记录购买数量，显示价格
                adapter.upDataNumber(amount);
                Sku sku = goods.getSkus().get(adapter.getSelsed());

                /**
                 * 2018.5.24
                 * 购买最大数量不超过库存
                 */
                number.setMaxNum(buynum(sku.getStocknum(),sku.getMaxCount()));
//                number.setMaxNum(sku.getStocknum());

                if (mapNumber.containsKey(sku.getSkuId())) {
                    mapNumber.put(sku.getSkuId(), amount);
                    goods.getSkus().get(adapter.getSelsed()).setNumber(amount);
                }
//                if (amount == sku.getStocknum()){
                if (amount == buynum(sku.getStocknum(),sku.getMaxCount())){
                    if (isshow) {
                        ViewUtils.showToastSuccess(R.string.maxbuy_num);
                        isshow = false;
                    }
                }
                tvMoney.setText("￥" + getCount());
            }
        });


        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String attr = goods.getSkus().get(position).getSkuAttr();
                int cunt = 0;
                if (attr.length() <= 5 && attr.length() > 0) {
                    cunt = 1;
                } else if (attr.length() > 5 && attr.length() < 10) {
                    cunt = 2;
                } else if (attr.length() >= 10) {
                    cunt = 4;
                }
                return cunt;
            }
        });
        adapter = new SkuDialogAdapter(getContext());
        adapter.setData(goods.getSkus());
        adapter.setNumber(number);
        adapter.setMapNumber(mapNumber);
        sku.setLayoutManager(manager);
        sku.setAdapter(adapter);

        /**
         * 2018.5.24
         * 购买最大数量不超过库存
         */
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                isshow = true;
                number.setSku(goods.getSkus().get(position));
                number.setMaxNum(buynum(goods.getSkus().get(position).getStocknum(),goods.getSkus().get(position).getMaxCount()));
            }
        });
    }

    /**
     * 2018.5.24
     * 购买最大数量不超过库存
     */
    private int buynum(int stocknum,int maxCount){
        int buynum;
        if (stocknum > maxCount && maxCount != 0){
            buynum = maxCount;
        }else {
            buynum = stocknum;
        }
        return buynum;
    }

    private void getNumberMap() {
        mapNumber = new HashMap<>();
        //从购物车中获得购买数量
        List<Goods> goodsList = ShopCart.getGoodsListSelected();
        //初始标记
        for (int i = 0; i < goods.getSkus().size(); i++) {
            mapNumber.put(goods.getSkus().get(i).getSkuId(), 0);
        }
        if (goodsList.size() > 0) {
            for (Goods good : goodsList) {
                if (good.getIds().equals(goods.getIds())) {
                    for (int i = 0; i < good.getSkus().size(); i++) {
                        mapNumber.put(good.getSkus().get(i).getSkuId(), good.getSkus().get(i).getNumber());
                        goods.getSkus().get(i).setNumber(good.getSkus().get(i).getNumber());
                    }
                }


            }
        }

        tvMoney.setText("￥" + getCount());
    }

    private String getCount() {
        double count = 0;
        for (Sku sku : goods.getSkus()) {
            if (mapNumber.containsKey(sku.getSkuId())) {
                count += sku.getUniformprice() * mapNumber.get(sku.getSkuId());
            }
        }
        return String.format("%.2f",count);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (cancelListener != null) {
            cancelListener.UnmberUpData(goods.getSkus());
        }
    }

    public interface CancelListener {
        void UnmberUpData(List<Sku> skus);
    }
}
