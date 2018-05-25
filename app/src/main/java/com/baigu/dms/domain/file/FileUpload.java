package com.baigu.dms.domain.file;


import android.support.annotation.Nullable;
import android.util.Log;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.SSLUtils;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.micky.logger.Logger;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:23
 */
public class FileUpload {

    String BOUNDARY = java.util.UUID.randomUUID().toString();
    String PREFIX = "--", LINEND = "\r\n";
    String MULTIPART_FROM_DATA = "multipart/form-data;";
    String CHARSET = "UTF-8";
    String requestName;

    /**
     * 同步上传
     *
     * @param urlStr   上传地址
     * @param filePath 文件路径
     * @param paramMap 参数，包含用户token，验证源valiType，调用来源标识 sysId
     * @param listener
     */
    public String upload(final String urlStr, final String filePath, final Map<String, String> paramMap, @Nullable final ProgressListener listener) {
        String result = null;
        File file = new File(filePath);
        if (file.exists()) {
            result = upload(urlStr, file, paramMap, listener);
        }
        return result;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    /**
     * 初始化用于鉴权的头
     */
    void initHttpheader(HttpURLConnection conn) {
        String appid = BaseApplication.getContext().getString(R.string.appid);
//        String token = TokenManager.getInstance().getToken();
        String token = SPUtils.getObject("token","");
        conn.setRequestProperty("appid", appid);
        conn.setRequestProperty("token", token);
    }

    /**
     * 上传文件同步方法
     *
     * @param urlStr   上传地址 s
     * @param file     上传的文件
     * @param params   参数，包含用户token，验证源valiType，调用来源标识 sysId
     * @param listener
     * @return 上传后文件的ID，NULL为上传失败
     */
    public String upload(String urlStr, File file, Map<String, String> params, @Nullable ProgressListener listener) {
        String result = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            if (urlStr != null && urlStr.startsWith("https://")) {
                HttpsURLConnection.setDefaultSSLSocketFactory(SSLUtils.getSSLSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(SSLUtils.getHostnameVerifier());
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setReadTimeout(10 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false);
            initHttpheader(conn);
            conn.setRequestMethod("POST"); // Post方式
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", CHARSET);
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + "boundary=" + BOUNDARY);
            conn.setChunkedStreamingMode(1024 * 1024);

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    putEntry(sb, entry.getKey(), entry.getValue());
                }
            }

            putEntry(sb, "size", String.valueOf(file.length()));
            outStream.write(sb.toString().getBytes());
            uploadFile(outStream, file, listener);
            outStream.flush();


            // 得到响应码
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();
//            InputStream in = conn.getErrorStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = null;
            StringBuffer data = new StringBuffer();
            if (res == HttpURLConnection.HTTP_OK) {
                while ((line = bufReader.readLine()) != null)
                    data.append(line);
            }

            JSONObject obj = new JSONObject(data.toString());
            if (obj != null && obj.has("status") && obj.has("data")) {
                if ("success".equals(obj.getString("status"))) {
                    result = obj.getString("data");
                }
            }

            in.close();
            outStream.close();
            conn.disconnect();
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return result;
    }

    void putEntry(StringBuilder buf, final String key, final String value) {
        buf.append(PREFIX);
        buf.append(BOUNDARY);
        buf.append(LINEND);
        buf.append("Content-Disposition: form-data; name=\""
                + key + "\"" + LINEND);
        buf.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
        buf.append(LINEND);
        buf.append(value);
        buf.append(LINEND);
    }

    void uploadFile(DataOutputStream outStream, final File file, final @Nullable ProgressListener listener) throws IOException {

        StringBuilder sb1 = new StringBuilder();
        sb1.append(PREFIX);
        sb1.append(BOUNDARY);
        sb1.append(LINEND);
        sb1.append("Content-Disposition: form-data; name=\""+requestName+"\"; filename=\"" + file.getName() + "\"" + LINEND);
        sb1.append("Content-Transfer-Encoding: binary" + LINEND);
        sb1.append(LINEND);
        outStream.write(sb1.toString().getBytes());


        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 4];
        int len = 0;
        long total = 0;
        int progress = 0;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
            total += len;
            int tempProgress = (int) ((total * 100) / file.length());
            if (tempProgress >= progress + 5 && tempProgress != 100) {
                progress = tempProgress;
                if (listener != null) {
                    listener.transferred(total);
                    listener.progress(progress);
                }
                Logger.i("Upload file " + file.getName() + ", current progress is " + progress);
            }
        }
        if (listener != null) {
            listener.transferred(total);
            listener.progress(100);
        }
        is.close();
        outStream.write(LINEND.getBytes());
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
    }
}
