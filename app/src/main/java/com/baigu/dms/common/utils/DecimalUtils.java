package com.baigu.dms.common.utils;

import java.text.DecimalFormat;

public class DecimalUtils {
    public static String wodecimal(double num){
        DecimalFormat df= new DecimalFormat("#.00");
        return df.format(num);
    }
}
