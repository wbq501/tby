package com.baigu.dms.common.utils.rxbus;

import java.io.Serializable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/18 12:18
 */
public class RxBusEvent implements Serializable {
    public int what;
    public Object object;
}
