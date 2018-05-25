package com.baigu.dms.domain.db.repository.impl;

import com.baigu.dms.domain.db.dao.CityDao;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.model.City;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:10
 */
public class CityRepositoryImpl extends BaseRepositoryImpl<City, String> implements CityRepository {

    public CityRepositoryImpl(AbstractDao dao) {
        super(dao);
    }

    @Override
    public List<City> queryByAreaType(int areaType) {
        return queryBuilder().where(CityDao.Properties.Type.eq(areaType)).list();
    }

    @Override
    public List<City> queryByParentId(String parentId) {
        return queryBuilder().where(CityDao.Properties.ParentId.eq(parentId)).list();
    }
}
