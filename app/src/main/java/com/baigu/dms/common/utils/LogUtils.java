package com.baigu.dms.common.utils;

import android.os.Environment;

import com.micky.logger.AndroidLogAdapter;
import com.micky.logger.CsvFormatStrategy;
import com.micky.logger.DiskLogAdapter;
import com.micky.logger.FormatStrategy;
import com.micky.logger.Logger;
import com.micky.logger.PrettyFormatStrategy;

import java.io.File;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/5 22:31
 */
public class LogUtils {

    private static final String TAG = "DMS";

    public static void initLogger() {

        Logger.clearLogAdapters();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(TAG).build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return Constants.DEBUG;
            }
        });

        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DMS" + File.separator + "log";
        FormatStrategy cvsFormatStrategy = CsvFormatStrategy.newBuilder()
                .tag(TAG)
                .logPath(logPath)
                .logFile("dms")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(cvsFormatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return Constants.DEBUG  || priority == Logger.ERROR;
            }
        });
    }
}
