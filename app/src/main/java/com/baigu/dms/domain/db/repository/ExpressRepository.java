package com.baigu.dms.domain.db.repository;

import com.baigu.dms.domain.model.Express;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 17:00
 */
public interface ExpressRepository extends BaseRepository<Express, String> {
    List<Express> queryAllOrdered();
}
