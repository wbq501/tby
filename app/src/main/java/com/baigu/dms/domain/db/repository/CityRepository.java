package com.baigu.dms.domain.db.repository;

import com.baigu.dms.domain.model.City;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:00
 */
public interface CityRepository extends BaseRepository<City, String> {
    List<City> queryByAreaType(int areaType);
    List<City> queryByParentId(String parentId);
}
