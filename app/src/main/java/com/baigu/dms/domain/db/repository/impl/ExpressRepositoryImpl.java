package com.baigu.dms.domain.db.repository.impl;

import com.baigu.dms.domain.db.dao.ExpressDao;
import com.baigu.dms.domain.db.repository.ExpressRepository;
import com.baigu.dms.domain.model.Express;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:10
 */
public class ExpressRepositoryImpl extends BaseRepositoryImpl<Express, String> implements ExpressRepository {

    public ExpressRepositoryImpl(AbstractDao dao) {
        super(dao);
    }

    @Override
    public List<Express> queryAllOrdered() {
        return queryBuilder().orderAsc(ExpressDao.Properties.Sort).list();
    }
}
