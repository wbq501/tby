package com.baigu.dms.domain.cache;

import android.content.Context;
import android.util.Log;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.model.Sku;
import com.micky.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/26 17:13
 */
public class ShopCart {
    private static ArrayList<Goods> sGoodsListSelected;

    public static ArrayList<Goods> getGoodsListSelected() {
        if (sGoodsListSelected == null) {
            sGoodsListSelected = new ArrayList<>();
        }
        return sGoodsListSelected;
    }

    public static void addGoods(Goods goods) {
        boolean hasNumber = false;
        int index = -1;
        for (int i = 0; i < getGoodsListSelected().size(); i++) {
            if (getGoodsListSelected().get(i).getIds().equals(goods.getIds())) {
                index = i;
            }
        }
        if (index != -1) {
            Goods goodsTemp = getGoodsListSelected().get(index);
            if (goodsTemp.getIds().equals(goods.getIds())) {
                for (Sku sku : goodsTemp.getSkus()) {
                    for (Sku msku : goods.getSkus()) {
                        if (sku.getSkuId().equals(msku.getSkuId())) {
                            sku.setNumber(msku.getNumber());
                        }
                    }
                }
            }
        } else {
            getGoodsListSelected().add(goods);
        }
        if (goods.getSkus().size() >= 1) {
            for (Sku sku : goods.getSkus()) {
                if (sku.getNumber() > 0) {
                    hasNumber = true;
                }
            }
            if (index != -1) {
                if (!hasNumber) {
                    getGoodsListSelected().remove(index);
                }
            }
        }
    }

    /**
     * 商品你检查: 商品[%1$s]和[%2$s]不能同时下单
     *
     * @param goods 选择的商品
     */
    public static boolean validGoodsRule(Context context, Goods goods, ConfirmDialog confirmDialog) {

        boolean result = true;

        for (Goods item : getGoodsListSelected()) {
            if (goods.getOrderflag() != item.getOrderflag()) {
                result = false;
                break;
            }
        }

        if (!result) {
            StringBuilder sb = new StringBuilder();
            for (Goods item : getGoodsListSelected()) {
                sb.append(item.getGoodsname()).append("、");
            }
            if (sb.length() > 2) {
                String validStr = sb.substring(0, sb.length() - 1);
                //移除不合格商品
                ShopCart.getGoodsListSelected().remove(goods);

                String invalidTip = context.getString(R.string.failed_goods_sum_faile_info, goods.getGoodsname(), validStr);
                confirmDialog.setTitle(invalidTip);
                confirmDialog.show();
            }
        }
        return result;
    }

    public static boolean validGoodsLimit(Context context, Goods goods, ConfirmDialog confirmDialog) {
        if (goods.getLimitnum() > 0 && goods.getBuyNum() >= goods.getLimitnum()) {
            String invalidTip = context.getString(R.string.goods_limit, goods.getGoodsname());
            confirmDialog.setTitle(invalidTip);
            confirmDialog.show();
            return false;
        }
        return true;
    }

    public static boolean validGoodsLimitGt(Context context, Goods goods, ConfirmDialog confirmDialog) {
        if (goods.getLimitnum() > 0 && goods.getBuyNum() > goods.getLimitnum()) {
            String invalidTip = context.getString(R.string.goods_limit, goods.getGoodsname());
            confirmDialog.setTitle(invalidTip);
            confirmDialog.show();
            return false;
        }
        return true;
    }

    public static boolean contains(Goods goods) {
        return getGoodsListSelected().contains(goods);
    }

    public static Goods getGoods(String goodsId) {
        for (Goods goods : getGoodsListSelected()) {
            if (goods.getIds().equals(goodsId)) {
                return goods;
            }
        }
        return null;
    }

    public static void clearCart() {
        getGoodsListSelected().clear();
    }

    public static void notifyDataChanged() {
        RxBus.getDefault().post(EventType.TYPE_SHOP_CART_CHANGED);
    }

    public static String getCount() {
        double count = 0;
        for (Goods goods : getGoodsListSelected()) {

            if (goods.getSkus().size() > 1) {
                for (Sku sku : goods.getSkus()) {
                    count += sku.getNumber() * sku.getUniformprice();
                }
            } else {
                Sku msku = goods.getSkus().get(0);
                count += msku.getNumber() * msku.getUniformprice();
            }
        }
        return String.format("%.2f", count);
    }

    public static void removeGoods(Goods goods) {
        if (getGoodsListSelected().contains(goods)) {
            getGoodsListSelected().remove(goods);
        }
    }

    public static boolean hasContains(Goods goods) {
        boolean result = false;
        for (Goods mGoods : getGoodsListSelected()) {
            if (mGoods.getIds().equals(goods.getIds())) {
                result = true;
            }
        }

        return result;
    }

    public static void checkGoods() {
        for (int i = 0; i < getGoodsListSelected().size(); i++) {
            boolean isRemove = true;
            List<Sku> skuList = getGoodsListSelected().get(i).getSkus();
            for (Sku sku : skuList) {
                if (sku.getNumber() > 0) {
                    isRemove = false;
                }
            }
            if (isRemove) {
                getGoodsListSelected().remove(i);
            }
        }
    }
}
