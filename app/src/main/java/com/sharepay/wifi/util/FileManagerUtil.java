package com.sharepay.wifi.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.helper.LogHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class FileManagerUtil {

    private static final String TAG = "FileManagerUtil ";
    private static FileManagerUtil mFileManagerUtil;
    private String mDownloadPath;
    private String mDownloadFilePath;
    private boolean mDownloadFileCreatSuccess = false;

    public static FileManagerUtil getInstance() {
        if (null == mFileManagerUtil) {
            mFileManagerUtil = new FileManagerUtil();
        }
        return mFileManagerUtil;
    }

    /**
     * 创建下载文件路径
     * 
     * @return
     */
    public boolean createDownloadPath() {
        try {
            mDownloadPath = getPrimaryStoragePath();
            if (isSaveData(mDownloadPath)) {
                mDownloadPath = mDownloadPath + File.separator + "SharePayWifi" + File.separator + "download";
                LogHelper.releaseLog(TAG + "createDownloadPath PrimaryStoragePath:" + mDownloadPath);
                File file = new File(mDownloadPath);
                if (!file.exists()) {
                    boolean createSuccess = file.mkdirs();
                    LogHelper.releaseLog(TAG + "createDownloadPath PrimaryStoragePath createSuccess:" + createSuccess);
                    if (createSuccess) {
                        mDownloadFileCreatSuccess = true;
                        return true;
                    }
                } else {
                    mDownloadFileCreatSuccess = true;
                    return true;
                }
            }
            mDownloadPath = getSecondaryStoragePath();
            if (isSaveData(mDownloadPath)) {
                mDownloadPath = mDownloadPath + File.separator + "SharePayWifi" + File.separator + "download";
                LogHelper.releaseLog(TAG + "createDownloadPath SecondaryStoragePath:" + mDownloadPath);
                File file = new File(mDownloadPath);
                if (!file.exists()) {
                    boolean createSuccess = file.mkdirs();
                    LogHelper.releaseLog(TAG + "createDownloadPath SecondaryStoragePath createSuccess:" + createSuccess);
                    if (createSuccess) {
                        mDownloadFileCreatSuccess = true;
                        return true;
                    }
                } else {
                    mDownloadFileCreatSuccess = true;
                    return true;
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "createDownloadPath Exception! msg:" + e.getMessage());
        }
        mDownloadFileCreatSuccess = false;
        return false;
    }

    /**
     * 可用空间
     * 
     * @param path
     * @return
     */
    private long getAvailableSize(String path) {
        LogHelper.releaseLog(TAG + "getAvailableSize path:" + path);
        try {
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            long blockSize = stat.getBlockSize();
            long blockCount = stat.getBlockCount();
            long availCount = stat.getAvailableBlocks();
            long availableSize = blockSize * availCount;
            LogHelper.releaseLog(TAG + "getAvailableSize blockSize:" + blockSize + " blockCount:" + blockCount + " totalSize:"
                    + blockSize * blockCount / (1024 * 1024) + "MB");
            LogHelper.releaseLog(TAG + "getAvailableSize availCount:" + availCount + " availableSize:" + availCount * blockSize / (1024 * 1024) + "MB");
            return availableSize;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getAvailableSize Exception! msg:" + e.getMessage());
        }
        return 0;
    }

    /**
     * 判断存储卡是否能保存数据
     * 
     * @param path
     * @return
     */
    public boolean isSaveData(String path) {
        LogHelper.releaseLog(TAG + "isSaveData path:" + path);
        if (!TextUtils.isEmpty(path) && TextUtils.equals(getStorageState(path), Environment.MEDIA_MOUNTED)) {
            long availableSize = getAvailableSize(path);
            long availableMSize = availableSize / (1024 * 1024);
            LogHelper.releaseLog(TAG + "isSaveData availableSize:" + availableSize + " availableMSize:" + availableMSize + "MB");
            if (availableMSize >= 50) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取主存储卡路径
     * 
     * @return
     */
    public String getPrimaryStoragePath() {
        try {
            StorageManager sm = (StorageManager) SPApplication.getContext().getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", null);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, null);
            LogHelper.releaseLog(TAG + "getPrimaryStoragePath:" + paths[0]);
            return paths[0];
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getPrimaryStoragePath() Exception! msg:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取次存储卡路径
     * 
     * @return
     */
    public String getSecondaryStoragePath() {
        try {
            StorageManager sm = (StorageManager) SPApplication.getContext().getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", null);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, null);
            String path = paths.length <= 1 ? null : paths[1];
            LogHelper.releaseLog(TAG + "getSecondaryStoragePath:" + path);
            return path;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getSecondaryStoragePath() Exception! msg:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取存储卡的挂载状态
     * 
     * @param path
     *            存储卡路径
     * @return
     */
    public String getStorageState(String path) {
        try {
            StorageManager sm = (StorageManager) SPApplication.getContext().getSystemService(Context.STORAGE_SERVICE);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", new Class[] { String.class });
            String state = (String) getVolumeStateMethod.invoke(sm, path);
            LogHelper.releaseLog(TAG + "getStorageState() state:" + state);
            return state;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getStorageState() Exception! msg:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取下载文件的路径
     * 
     * @return
     */
    public String getDownloadFilePath() {
        LogHelper.releaseLog(TAG + "getDownloadFilePath:" + mDownloadFilePath);
        return mDownloadFilePath;
    }

    /**
     * 在SD卡上创建文件
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createFile(String fileName) throws IOException {
        LogHelper.releaseLog(TAG + "createFile fileName:" + fileName + " mDownloadPath:" + mDownloadPath);
        boolean createFilePathResult = true;
        if (TextUtils.isEmpty(mDownloadPath)) {
            createFilePathResult = createDownloadPath();
        }
        LogHelper.releaseLog(TAG + "createFile createFilePathResult:" + createFilePathResult + " mDownloadFileCreatSuccess:" + mDownloadFileCreatSuccess);
        if (!createFilePathResult || !mDownloadFileCreatSuccess) {
            return null;
        }
        mDownloadFilePath = mDownloadPath + File.separator + "SharePayWifi.apk";
        LogHelper.releaseLog(TAG + "createFile mDownloadFilePath:" + mDownloadFilePath);
        File file = new File(mDownloadFilePath);
        boolean createFileResult = file.createNewFile();
        LogHelper.releaseLog(TAG + "createFile createFileResult:" + createFileResult);
        if (createFileResult) {
            mDownloadFilePath = file.getPath();
        }
        LogHelper.releaseLog(TAG + "createFile:" + createFileResult + " mDownloadFilePath:" + mDownloadFilePath + " mDownloadPath:" + mDownloadPath);
        return file;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile() {
        File file = new File(mDownloadFilePath);
        boolean isDelete = file.delete();
        return isDelete;
    }

    /**
     * 判断文件是否存在
     * 
     * @return
     */
    public boolean isFileExist(String fileName) {
        String path = mDownloadPath + File.separator + fileName;
        File file = new File(path);
        boolean isExists = file.exists();
        if (isExists) {
            mDownloadFilePath = path;
        }
        LogHelper.releaseLog(TAG + "isFileExist path:" + path + " isExists:" + isExists);
        return isExists;
    }

}
