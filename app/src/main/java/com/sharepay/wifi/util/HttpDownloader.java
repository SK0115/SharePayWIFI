package com.sharepay.wifi.util;

import android.content.Context;

import com.sharepay.wifi.helper.LogHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {

    private static final String TAG = "HttpDownloader ";

    private Context mContext;
    private String line = null;
    private StringBuffer strBuffer = new StringBuffer();
    private BufferedReader bufferReader = null;

    public HttpDownloader(Context context) {
        mContext = context;
    }

    /**
     * 下载小型的文档文件，返回文档的String字符串
     * 
     * @param urlStr
     * @return
     */
    public String downloadFiles(String urlStr) {
        try {
            InputStream inputStream = getInputStreamFromUrl(urlStr);
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferReader.readLine()) != null) {
                strBuffer.append(line + '\n');
            }
        } catch (Exception e) {
            strBuffer.append("something is wrong!!");
            LogHelper.errorLog(TAG + "download small Files Exception! msg:" + e.getMessage());
        } finally {
            try {
                bufferReader.close();
            } catch (Exception e) {
                strBuffer.append("something is wrong!!");
                e.printStackTrace();
            }
        }
        return strBuffer.toString();
    }

    /**
     * 可以下载任意文件，例如MP3，并把文件存储在制定目录（-1：下载失败，0：下载成功，1：文件已存在）
     * 
     * @param urlStr
     * @param path
     * @param fileName
     * @return
     */
    public int downloadFiles(String urlStr, String path, String fileName) {
        try {
            FileUtils fileUtils = new FileUtils();
            if (fileUtils.isFileExist(fileName, path)) {
                return 1;// 判断文件是否存在
            } else {
                InputStream inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = fileUtils.write2SDFromInput(fileName, path, inputStream);
                if (resultFile == null) {
                    return -1;
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "download any Files Exception! msg:" + e.getMessage());
            return -1;
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr) {
        try {
            // 创建一个URL对象
            URL url = new URL(urlStr);
            // 创建一个HTTP链接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 使用IO流获取数据
            InputStream inputStream = urlConn.getInputStream();
            return inputStream;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getInputStreamFromUrl Exception! msg:" + e.getMessage());
        }
        return null;
    }

    public File downLoadFile(String httpUrl) {
        final String fileName = "SharePayWifi.apk";
        String filePath = "/data/data/" + CommonUtil.getCurProcessName(mContext) + "/download";
        LogHelper.releaseLog(TAG + "downLoadFile filePath:" + filePath);
        File tmpFile = new File(filePath);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        final File file = new File(filePath + "/" + fileName);
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[256];
            conn.connect();
            double count = 0;
            if (conn.getResponseCode() >= 400) {
                // Toast.makeText(Main.this, "连接超时", Toast.LENGTH_SHORT).show();
            } else {
                while (count <= 100) {
                    if (is != null) {
                        int numRead = is.read(buf);
                        if (numRead <= 0) {
                            break;
                        } else {
                            fos.write(buf, 0, numRead);
                        }

                    } else {
                        break;
                    }

                }
            }
            conn.disconnect();
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.errorLog(TAG + "downLoadFile Exception! msg:" + e.getMessage());
        }
        return file;
    }
}
