package com.sharepay.wifi.util;

import com.sharepay.wifi.helper.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {

    private static final String TAG = "HttpDownloader ";

    /**
     * 下载文件
     * 
     * @param httpUrl
     * @param fileName
     * @return
     */
    public File downloadFile(String httpUrl, String fileName) {
        LogHelper.releaseLog(TAG + "downloadFile downloadFile:" + fileName);
        File file = null;
        InputStream input = null;
        FileOutputStream output = null;
        try {
            FileManagerUtil.getInstance().createDownloadPath();
            if (FileManagerUtil.getInstance().isFileExist(fileName)) {
                file = new File(FileManagerUtil.getInstance().getDownloadFilePath());
                LogHelper.releaseLog(TAG + "downloadFile isFileExist! file:" + file);
                if (null != file) {
                    FileManagerUtil.getInstance().setDownloadSuccess(true);
                    return file;
                }
            }
            file = FileManagerUtil.getInstance().createFile(fileName);
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.connect();
            input = conn.getInputStream();
            output = new FileOutputStream(file);
            int numRead;
            byte buf[] = new byte[1536];
            while ((numRead = input.read(buf)) >= 0) {
                output.write(buf, 0, numRead);
                output.flush();
            }
            FileManagerUtil.getInstance().setDownloadSuccess(true);
            conn.disconnect();
        } catch (Exception e) {
            FileManagerUtil.getInstance().setDownloadSuccess(false);
            LogHelper.errorLog(TAG + "downloadFile Exception! msg:" + e.getMessage());
        } finally {
            try {
                output.close();
                input.close();
            } catch (Exception e) {
                FileManagerUtil.getInstance().setDownloadSuccess(false);
                LogHelper.errorLog(TAG + "downloadFile OutputStream Exception! msg:" + e.getMessage());
            }
        }
        return file;
    }
}
