package com.baigu.dms.domain.netservice.common.model;

import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.ExclusiveGroups;
import com.baigu.dms.domain.model.Express;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 15:06
 */
public class BasicDataResult {
    /**快递*/
    public List<Express> expressList;
    /**地区*/
    public List<City> areaList;

    public List<BankType> bankList;

    public List<ExclusiveGroups> exclusiveGroups;
    public int version;
}
