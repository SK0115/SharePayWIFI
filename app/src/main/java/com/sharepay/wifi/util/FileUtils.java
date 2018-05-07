package com.sharepay.wifi.util;

import android.os.Environment;

import com.sharepay.wifi.helper.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static final String TAG = "FileUtils ";
    private String mSDCardRoot;

    public FileUtils() {
        // 得到当前外部存储设备的目录
        mSDCardRoot = Environment.getExternalStorageDirectory() + File.separator;
        LogHelper.releaseLog(TAG + "mSDCardRoot:" + mSDCardRoot);
        // File.separator为文件分隔符”/“,方便之后在目录下创建文件
    }

    /**
     * 在SD卡上创建文件
     * 
     * @param fileName
     * @param dir
     * @return
     * @throws IOException
     */
    public File createFileInSDCard(String fileName, String dir) throws IOException {
        // File file = new File(mSDCardRoot + dir + File.separator + fileName);
        File file = new File("/data/data/com.sharepay.wifi/download/SharePayWifi.apk");
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     * 
     * @param dir
     * @return
     */
    public File createSDDir(String dir) {
        try {
            File dirFile = new File(mSDCardRoot + dir);
            dirFile.mkdir();// mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录
            return dirFile;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "createSDDir Exception! msg:" + e.getMessage());
        }
        return null;
    }

    /**
     * 判断文件是否存在
     * 
     * @param fileName
     * @param dir
     * @return
     */
    public boolean isFileExist(String fileName, String dir) {
        File file = new File(mSDCardRoot + dir + File.separator + fileName);
        return file.exists();
    }

    /**
     * 将一个InoutStream里面的数据写入到SD卡中
     * 
     * @param fileName
     * @param dir
     * @param input
     * @return
     */
    public File write2SDFromInput(String fileName, String dir, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            // 创建目录
            createSDDir(dir);
            // 创建文件
            file = createFileInSDCard(fileName, dir);
            // 写数据流
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];// 每次存4K
            int temp;
            // 写入数据
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "write2SDFromInput Exception! msg:" + e.getMessage());
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                LogHelper.errorLog(TAG + "write2SDFromInput OutputStream Exception! msg:" + e.getMessage());
            }
        }
        return file;
    }
}
