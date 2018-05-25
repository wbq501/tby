package com.baigu.dms.domain.db.repository;

import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface BankTypeRepository extends  BaseRepository<BankType,String> {
    List<BankType> queryAllBank();
}
