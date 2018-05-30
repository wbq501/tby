package com.baigu.dms.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.model.ExclusiveGroups;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BuyGoodsType {
    /**
     * 2018.5.25  购买互斥
     * @param sku
     * @param isremove 是否移除某个系列的标记
     * @param isMore 是否是多种规格
     * @return true 可以买这个系列
     */
    public static boolean isBuy(Sku sku, boolean isremove, boolean isMore){
        List<Set<String>> rules = getRules();
        LinkedHashSet<String> all = SPUtils.getDataList("buyType");
        Toast.makeText(BaseApplication.getContext(),"读取当前已经存储的类型："+all.toString()+"\n"
                +"当前选择类型"+sku.getGroup(),Toast.LENGTH_SHORT).show();
        if (isremove){
            if (ShopCart.getGoodsListSelected().size() > 0){
                Set<String> buyall = new LinkedHashSet<>();//新建已经加入购物车的类型
                String[] skugroup = sku.getGroup().split(",");//拿到点击减号类型

                for (Goods good : ShopCart.getGoodsListSelected()) {
                    List<Sku> skus = good.getSkus();
                    for (int i = 0; i < skus.size(); i++){
                        if (sku.getNumber() == skus.get(i).getNumber() && sku.getSkuId().equals(skus.get(i).getSkuId())){
                           break;
                        }
                        if (skus.get(i).getNumber() >  0){
                            String[] split = skus.get(i).getGroup().split(",");
                            for (int j = 0; j < split.length; j++){
                                buyall.add(split[j]);
                            }
                        }
                    }
                }
                for (int j = 0; j < skugroup.length; j++){
                    if (!buyall.contains(skugroup[j])){
                        if (all.contains(skugroup[j])){
                            all.remove(skugroup[j]);
                        }
                    }
                }
            }else {
                if (all.size() > 0){
                    String[] skugroup = sku.getGroup().split(",");
                    for (int i = 0;i < skugroup.length; i++){
                        if (all.contains(skugroup[i])){
                            all.remove(skugroup[i]);
                        }
                    }
                }else {
                    return false;
                }
            }
        }else {
            if (sku != null){
                Set<String> allnew = new LinkedHashSet<>();//新建临时选择好的数组
                String[] group = new String[0];//临时数组用来存储选择好的类型
                String[] split = sku.getGroup().split(",");
                group = concat(group,split);
                if (group.length <= 0){
                    return false;
                }
                for (int i = 0; i < group.length; i++){
                    allnew.add(group[i]);
                }
                allnew.addAll(all);
                for (Set<String> ruleSet : rules) {
                    int matchCount = 0;
                    for (String group1 : ruleSet) {
                        for (String group2 : allnew) {
                            if (group1.equals(group2)) {
                                matchCount++;
                                break;
                            }
                        }
                    }
                    if(matchCount >= ruleSet.size()) {//表示互斥了
//                        for (int i = 0; i < group.length; i++){
//                            if (all.contains(group[i]))
//                                all.remove(group[i]);
//                        }
                        return false;
                    }
                }
                all.addAll(allnew);
            }else {
                return false;
            }
        }
        SPUtils.setDataList("buyType",all);//临时存储
        if (all.size() <= 0){
            SPUtils.clearBuyType();
        }
        Set<String> buyType = SPUtils.getDataList("buyType");
        Toast.makeText(BaseApplication.getContext(),"存储已经选了有的类型："+buyType.toString(),Toast.LENGTH_SHORT).show();
        return true;
    }


    /**
     * 互斥规则集合
     * @return
     */
    private static List<Set<String>> getRules(){
        List<Set<String>> rules = new ArrayList<>();
        List<ExclusiveGroups> exclusiveGroups = RepositoryFactory.getInstance().getExclusiveGroupRepository().queryAllBank();
        for (int i = 0; i < exclusiveGroups.size(); i++){
            String[] value = exclusiveGroups.get(i).getValue().split(",");
            Set<String> rule = new HashSet<>();
            for (int j = 0; j < value.length; j++){
                rule.add(value[j]);
            }
            rules.add(rule);
        }
        return rules;
    }


    private static String[] concat(String[] a, String[] b){
        String[] c = new String[a.length+b.length];
        System.arraycopy(a,0,c,0,a.length);
        System.arraycopy(b,0,c,a.length,b.length);
        return c;
    }
}
