package com.baigu.dms.domain.db.repository.impl;

import com.baigu.dms.domain.db.dao.BankTypeDao;
import com.baigu.dms.domain.db.dao.ExclusiveGroupsDao;
import com.baigu.dms.domain.db.repository.ExclusiveGroupsRepository;
import com.baigu.dms.domain.model.ExclusiveGroups;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class ExclusiveGroupsRepositoryImpl extends BaseRepositoryImpl<ExclusiveGroups,String> implements ExclusiveGroupsRepository{
    public ExclusiveGroupsRepositoryImpl(AbstractDao dao) {
        super(dao);
    }

    @Override
    public List<ExclusiveGroups> queryAllBank() {
        return queryBuilder().orderAsc(ExclusiveGroupsDao.Properties.Sort).list();
    }
}
