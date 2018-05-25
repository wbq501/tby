package com.baigu.dms.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 19:31
 */
public class Constants {
    public static final boolean DEBUG = true;

    //网络相关
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    public static ExecutorService sExecutorService = Executors.newFixedThreadPool(5);

    /**图片选择最大数*/
    public static final int IMAGE_PICKER_MAX_SELECT = 9;

    public static final int AUTH_CODE_TIME = 120; //验证码倒计时

    public static final int BUTTON_UNABLE_ALPHA = 100;

    //*/ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态

    //    public static final String GESTURE_PASSWROD = "gesturePsd";
    public static final String IS_BACKGROUND = "isbackground";
}
