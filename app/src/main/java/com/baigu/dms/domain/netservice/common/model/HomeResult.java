package com.baigu.dms.domain.netservice.common.model;

import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Notice;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.RecommendClass;

import java.util.List;

/**
 * @Description 首页数据
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/7/11 22:24
 */
public class HomeResult {
    public List<Advert> advertislist;
    public List<Notice> cbullitinlist;
    public List<Advert> bannerlist;
    public List<Goods> superGoodsList;
    public List<BrandStory> brandList;
    public List<RecommendClass>  recommendCategories;
    public List<Advert> bannerList;

    @Override
    public String toString() {
        return "HomeResult{" +
                "advertislist=" + advertislist +
                ", cbullitinlist=" + cbullitinlist +
                ", bannerlist=" + bannerlist +
                ", superGoodsList=" + superGoodsList +
                ", brandList=" + brandList +
                ", recommendCategories=" + recommendCategories +
                '}';
    }
}
