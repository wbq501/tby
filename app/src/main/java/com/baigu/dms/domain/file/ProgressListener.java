package com.baigu.dms.domain.file;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:23
 */
public interface ProgressListener {
    /**传输字节数*/
    void transferred(long num);

    /**进度更新*/
    void progress(Integer p);
}


