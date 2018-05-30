package com.baigu.dms.domain.db;


import android.util.Log;

import com.baigu.dms.domain.db.dao.BankTypeDao;
import com.baigu.dms.domain.db.dao.CityDao;
import com.baigu.dms.domain.db.dao.ExclusiveGroupsDao;
import com.baigu.dms.domain.db.dao.ExpressDao;
import com.baigu.dms.domain.db.repository.BankTypeRepository;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.db.repository.ExclusiveGroupsRepository;
import com.baigu.dms.domain.db.repository.ExpressRepository;
import com.baigu.dms.domain.db.repository.impl.BankRepositoryImpl;
import com.baigu.dms.domain.db.repository.impl.CityRepositoryImpl;
import com.baigu.dms.domain.db.repository.impl.ExclusiveGroupsRepositoryImpl;
import com.baigu.dms.domain.db.repository.impl.ExpressRepositoryImpl;

public class RepositoryFactory {

    private static class RepositoryFactoryHolder {
        private static final RepositoryFactory INSTANCE = new RepositoryFactory();
    }

    private RepositoryFactory() {
    }

    public static final RepositoryFactory getInstance() {
        return RepositoryFactoryHolder.INSTANCE;
    }


    public CityRepository getCityRepository() {
        if (DBCore.getDaoSession() == null) {
            return null;
        }
        CityDao districtDao = DBCore.getDaoSession().getCityDao();
        return new CityRepositoryImpl(districtDao);
    }

    public ExpressRepository getExpressRepository() {
        if (DBCore.getDaoSession() == null) {
            return null;
        }
        ExpressDao expressDao = DBCore.getDaoSession().getExpressDao();
        return new ExpressRepositoryImpl(expressDao);
    }

    public BankTypeRepository getBankRepository() {
        if (DBCore.getDaoSession() == null) {
            return null;
        }

        BankTypeDao bankDao = DBCore.getDaoSession().getBankTypeDao();
        return new BankRepositoryImpl(bankDao);
    }

    public ExclusiveGroupsRepository getExclusiveGroupRepository(){
        if (DBCore.getDaoSession() == null){
            return null;
        }

        ExclusiveGroupsDao exclusiveGroupsDao = DBCore.getDaoSession().getExclusiveGroupsDao();
        return new ExclusiveGroupsRepositoryImpl(exclusiveGroupsDao);
    }
}
