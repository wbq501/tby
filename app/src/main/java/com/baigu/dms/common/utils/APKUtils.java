package com.baigu.dms.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.micky.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @Package org.kteam.palm.common.utils
 * @Project Palm
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Team KTEAM
 * @Date 2015-12-27 23:39
 */
public class APKUtils {

    public static final int HANDLER_MSG_PD_UPDATE = 1000;
    public static final int HANDLER_MSG_DOWNLOAD_COMPLET = 1001;
    public static final int HANDLER_MSG_DOWNLOAD_FAILED = 1002;

    private static final String PACKAGE_NAME = "com.baigu.dms";

    private Context mContext;

    public APKUtils(Context context) {
        mContext = context;
    }

    /**
     * 获得本地版本号
     */
    public Version getLocalVersionCode(){
        Version version = null;
        try {
            int versionCode = mContext.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionCode;
            String versionName = mContext.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionName;
            version = new Version();
            version.versionCode = versionCode;
            version.versionName = versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e, e.getMessage());
        }
        return version;
    }

    /**
     * 获得本地版本号
     */
    public static Version getLocalVersionCode(Context context){
        Version version = null;
        try {
            int versionCode = context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionCode;
            String versionName = context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionName;
            version = new Version();
            version.versionCode = versionCode;
            version.versionName = versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 下载APK文件
     */
    public void downFile(final String url, final String localDir, final String fileName, final Handler handler) {
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                download(url, localDir, fileName, handler);
            }
        });
    }

    /**
     * 下载
     * @param urlStr apk url地址
     * @param localDir 本地路径
     * @param fileName 本地文件名
     * @param handler
     * @return
     */
    public boolean download(String urlStr, final String localDir, final String fileName, final Handler handler) {
        boolean result = false;
        HttpURLConnection conn = null;
        FileOutputStream fos = null;
        InputStream is = null;
        File destFile = null;
        File tmpFile = null;
        int progress = 0;
        try {
            URL url = new URL(urlStr);
            if (urlStr != null && urlStr.startsWith("https://")) {
                HttpsURLConnection.setDefaultSSLSocketFactory(SSLUtils.getSSLSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(SSLUtils.getHostnameVerifier());
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(1000 * 30);
            conn.setReadTimeout(1000 * 30);

            int fileLength = conn.getContentLength();

            File dirFile = new File(new String(localDir.getBytes("UTF-8"), "UTF-8"));
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                dirFile.mkdirs();
            }

            destFile = new File(localDir, new String(fileName.getBytes("UTF-8"), "UTF-8"));
            if (destFile != null && destFile.exists() && destFile.length() > (1024 * 1024 * 5)) {
                result = true;
                return result;
            }

            tmpFile = new File(localDir, new String(String.valueOf(System.currentTimeMillis()).getBytes("UTF-8"), "UTF-8"));
            if (tmpFile.exists()) {
                tmpFile.delete();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_RESET) {
                fos = new FileOutputStream(tmpFile);
                is = conn.getInputStream();

                byte[] buffer = new byte[1024 * 4];
                int len = 0;
                long total = 0;

                while ((len = is.read(buffer)) > 0) {
                    total += len;
                    fos.write(buffer, 0, len);
                    int tempProgress = (int) ((total * 100) / fileLength);
                    if (tempProgress >= progress + 5 && tempProgress != 100) {
                        progress = tempProgress;
                        Message msg = handler.obtainMessage();
                        msg.what = HANDLER_MSG_PD_UPDATE;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                }
                if (destFile.exists()) {
                    destFile.delete();
                }
                FileUtils.copyFile(tmpFile, destFile, true);
                result = true;
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        } finally {
            Message msg = handler.obtainMessage();
            msg.what = result ? HANDLER_MSG_DOWNLOAD_COMPLET : HANDLER_MSG_DOWNLOAD_FAILED;
            msg.obj = localDir + File.separator + fileName;
            handler.sendMessage(msg);
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {

                }
            }

            try {
                if (tmpFile != null && tmpFile.exists()) {
                    tmpFile.delete();
                }
            } catch (Exception e) {

            }

            //下载失败，删除已生成的文件
            if (!result) {
                destFile.delete();
            }

        }
        return result;
    }

    public void deleteDownloadApk() {
        try {
            FileUtils.deleteDir(FileUtils.APK_LOCAL_PATH);
        } catch (Exception e) {
        }
    }

    /**
     * 安装应用
     * @param packagePath
     * @return
     */
    public boolean installApp(String packagePath) {
        try {
            if(TextUtils.isEmpty(packagePath)) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(packagePath)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return true;
        } catch(Exception e) {
            Logger.e(e, e.getMessage());
        }
        return false;
    }


    /**
     * 卸载应用
     * @param pakageName
     */
    public void uninstallApp(String pakageName) {
        Uri packageURI = Uri.parse("package:"+pakageName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(packageURI);
        mContext.startActivity(intent);
    }

    public static class Version {
        public int versionCode;
        public String versionName;
    }


    public static void toAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }
}
