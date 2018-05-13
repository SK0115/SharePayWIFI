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
                return file;
            }
            file = FileManagerUtil.getInstance().createFile(fileName);
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            input = conn.getInputStream();
            output = new FileOutputStream(file);
            byte[] buf = new byte[256];
            conn.connect();
            double count = 0;
            if (conn.getResponseCode() >= 400) {
            } else {
                while (count <= 100) {
                    if (input != null) {
                        int numRead = input.read(buf);
                        if (numRead <= 0) {
                            break;
                        } else {
                            output.write(buf, 0, numRead);
                        }

                    } else {
                        break;
                    }
                }
            }
            output.flush();
            conn.disconnect();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "downloadFile Exception! msg:" + e.getMessage());
        } finally {
            try {
                output.close();
                input.close();
            } catch (Exception e) {
                LogHelper.errorLog(TAG + "downloadFile OutputStream Exception! msg:" + e.getMessage());
            }
        }
        return file;
    }
}
