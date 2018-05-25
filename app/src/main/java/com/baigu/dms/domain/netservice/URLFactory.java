package com.baigu.dms.domain.netservice;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 21:33
 */
public class URLFactory {

    private static class URLFactoryHolder {
        private static URLFactory sInstance = new URLFactory();
    }

    private URLFactory() {
    }

    public static final URLFactory getInstance() {
        return URLFactory.URLFactoryHolder.sInstance;
    }

    public String getRegisterAgreement() {
        return BaseApplication.getContext().getString(R.string.end_point) + BaseApplication.getContext().getString(R.string.url_register_agreement);
    }

    public String getUseAgreement() {
        return BaseApplication.getContext().getString(R.string.end_point) + BaseApplication.getContext().getString(R.string.url_use_agreement);
    }

}
