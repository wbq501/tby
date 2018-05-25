package com.baigu.dms.common.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/19 22:51
 */
public class AllCapTransformationMethod extends ReplacementTransformationMethod {

    @Override
    protected char[] getOriginal() {
        char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        return cc;
    }

}