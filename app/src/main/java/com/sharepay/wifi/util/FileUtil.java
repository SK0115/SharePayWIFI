package com.sharepay.wifi.util;

import android.content.Context;
import android.os.Environment;

import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.helper.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    private static final String TAG = "FileUtil ";
     private String mSDCardRoot;

    public FileUtil() {
        // 得到当前外部存储设备的目录
         mSDCardRoot = Environment.getExternalStorageDirectory() + File.separator;
        // LogHelper.releaseLog(TAG + "mSDCardRoot:" + mSDCardRoot);
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
//         File file = new File(mSDCardRoot + File.separator +"SharePayWifi" + File.separator+ "download" + File.separator + "SharePayWifi.apk");
        // File file = new File("/SharePayWifi/download/SharePayWifi.apk");
        String path = SPApplication.getContext().getFilesDir().getAbsolutePath();
        File file = new File(path + File.separator + "SharePayWifi" + File.separator + "download" + File.separator + "SharePayWifi.apk");
        LogHelper.releaseLog(TAG + "createFileInSDCard:" + file.createNewFile());
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
            String path = SPApplication.getContext().getFilesDir().getAbsolutePath();
            // File dirFile = new File(mSDCardRoot + dir);
            // File dirFile = new File("/SharePayWifi/download");
            File dirFile = new File(path + File.separator + "SharePayWifi" + File.separator + "download");
            dirFile.mkdirs();// mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录
            LogHelper.releaseLog(TAG + "createSDDir:" + dirFile.mkdirs() + " path:" + path);
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
     * @param path
     * @return
     */
    public boolean isFileExist(String fileName, String path) {
        // File file = new File(mSDCardRoot + path + File.separator + fileName);
        // File file = new File("/SharePayWifi/download/SharePayWifi.apk");
        String path1 = SPApplication.getContext().getFilesDir().getAbsolutePath();
        File file = new File(path1 + File.separator +"SharePayWifi" + File.separator+ "download" + File.separator + "SharePayWifi.apk");
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
//        OutputStream output = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 创建目录
            createSDDir(dir);
            // 创建文件
            file = createFileInSDCard(fileName, dir);
            // 写数据流
//            output = new FileOutputStream(file, Context.MODE_PRIVATE);
            fileOutputStream = SPApplication.getContext().openFileOutput(file.getAbsolutePath(), Context.MODE_PRIVATE);
            byte buffer[] = new byte[4 * 1024];// 每次存4K
            int temp;
            // 写入数据
            while ((temp = input.read(buffer)) != -1) {
//                output.write(buffer, 0, temp);
                fileOutputStream.write(buffer, 0, temp);
            }
//            output.flush();
            fileOutputStream.flush();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "write2SDFromInput Exception! msg:" + e.getMessage());
        } finally {
            try {
//                output.close();
                fileOutputStream.close();
                input.close();
            } catch (Exception e) {
                LogHelper.errorLog(TAG + "write2SDFromInput OutputStream Exception! msg:" + e.getMessage());
            }
        }
        return file;
    }
}
