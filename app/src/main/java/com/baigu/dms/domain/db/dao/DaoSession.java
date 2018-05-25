package com.baigu.dms.domain.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.baigu.dms.domain.model.Express;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.City;

import com.baigu.dms.domain.db.dao.ExpressDao;
import com.baigu.dms.domain.db.dao.BankTypeDao;
import com.baigu.dms.domain.db.dao.CityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig expressDaoConfig;
    private final DaoConfig bankTypeDaoConfig;
    private final DaoConfig cityDaoConfig;

    private final ExpressDao expressDao;
    private final BankTypeDao bankTypeDao;
    private final CityDao cityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        expressDaoConfig = daoConfigMap.get(ExpressDao.class).clone();
        expressDaoConfig.initIdentityScope(type);

        bankTypeDaoConfig = daoConfigMap.get(BankTypeDao.class).clone();
        bankTypeDaoConfig.initIdentityScope(type);

        cityDaoConfig = daoConfigMap.get(CityDao.class).clone();
        cityDaoConfig.initIdentityScope(type);

        expressDao = new ExpressDao(expressDaoConfig, this);
        bankTypeDao = new BankTypeDao(bankTypeDaoConfig, this);
        cityDao = new CityDao(cityDaoConfig, this);

        registerDao(Express.class, expressDao);
        registerDao(BankType.class, bankTypeDao);
        registerDao(City.class, cityDao);
    }
    
    public void clear() {
        expressDaoConfig.clearIdentityScope();
        bankTypeDaoConfig.clearIdentityScope();
        cityDaoConfig.clearIdentityScope();
    }

    public ExpressDao getExpressDao() {
        return expressDao;
    }

    public BankTypeDao getBankTypeDao() {
        return bankTypeDao;
    }

    public CityDao getCityDao() {
        return cityDao;
    }

}