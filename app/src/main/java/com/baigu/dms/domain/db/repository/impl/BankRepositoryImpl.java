package com.baigu.dms.domain.db.repository.impl;

import com.baigu.dms.domain.db.dao.BankTypeDao;
import com.baigu.dms.domain.db.repository.BankTypeRepository;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class BankRepositoryImpl extends  BaseRepositoryImpl<BankType,String> implements BankTypeRepository {
    public BankRepositoryImpl(AbstractDao dao) {
        super(dao);
    }

    @Override
    public List<BankType> queryAllBank() {
        return queryBuilder().orderAsc(BankTypeDao.Properties.Sort).list();
    }
}
