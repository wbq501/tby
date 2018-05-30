package com.baigu.dms.domain.db.repository;

import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.ExclusiveGroups;

import java.util.List;

public interface ExclusiveGroupsRepository extends BaseRepository<ExclusiveGroups,String>{
    List<ExclusiveGroups> queryAllBank();
}
