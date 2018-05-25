package com.baigu.dms.domain.netservice.common.model;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/22 22:45
 */
public class PageResult<T> {
    public int totalRow;
    public int totalPage;
    public boolean lastPage;
    public int pageSize;
    public int pageNumber;
    public boolean firstPage;
    public List<T> list;
}
