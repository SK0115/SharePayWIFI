package com.sharepay.wifi.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.helper.RealmHelper;
import com.sharepay.wifi.model.http.ShareWifiHttpData;
import com.sharepay.wifi.model.http.TokenHttpData;
import com.sharepay.wifi.model.realm.DeviceInfoRealm;
import com.sharepay.wifi.model.realm.SignInfoRealm;
import com.sharepay.wifi.model.realm.TokenInfoRealm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;

public class CommonUtil {

    private static final String TAG = "CommonUtil ";

    private static final long EXPIRED_TIME = 30 * 24 * 60 * 60;

    public static <T> boolean checkIsNull(T reference) {
        if (null == reference) {
            return true;
        }
        return false;
    }

    /**
     * 获取设备唯一码
     * 
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceUniqueId() {
        String uniqueId = "";
        String androidID = Settings.System.getString(SPApplication.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        uniqueId = androidID + Build.SERIAL;
        LogHelper.releaseLog(TAG + "getDeviceUserID androidID:" + androidID + " ser:" + Build.SERIAL);
        try {
            return toMD5(uniqueId);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getDeviceUserID Exception! message:" + e.getMessage());
        }
        return uniqueId;
    }

    /**
     * 获取设备token信息
     * 
     * @return
     */
    public static String getToken() {
        String token = "";
        TokenInfoRealm tokenInfoRealm = new TokenInfoRealm();
        RealmObject realmObject = RealmHelper.getInstance().queryRealmObjectByValue(tokenInfoRealm, "tokenKey", TokenInfoRealm.TOKENINFO_KEY);
        if (realmObject instanceof TokenInfoRealm) {
            tokenInfoRealm = (TokenInfoRealm) realmObject;
            token = tokenInfoRealm.getToken();
            LogHelper.releaseLog(TAG + "getToken tokenInfoRealm:" + tokenInfoRealm.toString());
        }
        token = decryptBase64(token);
        LogHelper.releaseLog(TAG + "getToken decryptBase64 token:" + token);
        RealmHelper.getInstance().close();
        return token;
    }

    /**
     * 保存设备token信息
     */
    public static void saveToken(TokenHttpData tokenData) {
        if (null != tokenData) {
            TokenInfoRealm tokenInfoRealm = new TokenInfoRealm();
            tokenInfoRealm.setToken(encryptBase64(tokenData.getToken()));
            tokenInfoRealm.setTimeStamp(System.currentTimeMillis());
            tokenInfoRealm.setTimeString(CommonUtil.getTimeFormat(tokenInfoRealm.getTimeStamp(), ""));
            LogHelper.releaseLog(TAG + "saveToken tokenInfoRealm:" + tokenInfoRealm.toString());
            if (RealmHelper.getInstance().isRealmObjectExist(tokenInfoRealm, "tokenKey", TokenInfoRealm.TOKENINFO_KEY)) {
                RealmHelper.getInstance().updateRealmObject(tokenInfoRealm);
            } else {
                RealmHelper.getInstance().addRealmObject(tokenInfoRealm);
            }
            RealmHelper.getInstance().close();
        }
    }

    /**
     * 根据具体时间以及时间类型，获取时间戳
     * 
     * @param strTime
     *            时间字符串
     * @param formatType
     *            时间字符串的格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getTimeStamp(String strTime, String formatType) {
        LogHelper.releaseLog(TAG + "getTimeStamp strTime:" + strTime + " formatType:" + formatType);
        if (TextUtils.isEmpty(formatType)) {
            formatType = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
            LogHelper.releaseLog(TAG + "getTimeStamp date:" + date);
        } catch (ParseException e) {
            LogHelper.errorLog(TAG + "getTimeStamp Exception! message:" + e.getMessage());
        }
        if (null == date) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    /**
     * 根据时间戳以及时间类型，获取具体时间
     * 
     * @param milliseconds
     *            时间戳
     * @param formatType
     *            转换格式
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeFormat(long milliseconds, String formatType) {
        LogHelper.releaseLog(TAG + "getTimeFormat milliseconds:" + milliseconds + " formatType:" + formatType);
        if (TextUtils.isEmpty(formatType)) {
            formatType = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(formatType);
        try {
            LogHelper.releaseLog(TAG + "getTimeFormat time:" + df.format(new Date(milliseconds)));
            return df.format(new Date(milliseconds));
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getTimeFormat Exception! message:" + e.getMessage());
        }
        return "";
    }

    /**
     * Base64加密
     * 
     * @param str
     *            需要加密的字符串
     * @return
     */
    public static String encryptBase64(String str) {
        String encodedString = "";
        if (!TextUtils.isEmpty(str)) {
            encodedString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        }
        return encodedString;
    }

    /**
     * Base64解密
     * 
     * @param str
     *            需要解密的字符串
     * @return
     */
    public static String decryptBase64(String str) {
        String decodedString = "";
        if (!TextUtils.isEmpty(str)) {
            decodedString = new String(Base64.decode(str, Base64.DEFAULT));
        }
        return decodedString;
    }

    /**
     * 将字符串转换成MD5
     * 
     * @param text
     *            需要转换的字符串
     * @return
     * @throws Exception
     */
    public static String toMD5(String text) throws Exception {
        // 获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        // 通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            // 循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            // 将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            // 转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            // 将循环结果添加到缓冲区
            sb.append(hexString);
        }
        // 返回整个结果
        LogHelper.releaseLog(TAG + "toMD5 string:" + sb.toString() + " text:" + text);
        return sb.toString();
    }

    /**
     * 判断token是否过期
     * 
     * @return
     */
    public static boolean tokenIsExpired() {
        TokenInfoRealm tokenInfoRealm = new TokenInfoRealm();
        RealmObject realmObject = RealmHelper.getInstance().queryRealmObjectByValue(tokenInfoRealm, "tokenKey", TokenInfoRealm.TOKENINFO_KEY);
        RealmHelper.getInstance().close();
        if (realmObject instanceof TokenInfoRealm) {
            tokenInfoRealm = (TokenInfoRealm) realmObject;
            LogHelper.releaseLog(TAG + "tokenIsExpired string:" + tokenInfoRealm.toString());
            if (!TextUtils.isEmpty(tokenInfoRealm.getTokenKey())) {
                long timeStamp = tokenInfoRealm.getTimeStamp();
                long curTimeStamp = System.currentTimeMillis();
                LogHelper.releaseLog(TAG + "tokenIsExpired time:" + (timeStamp + EXPIRED_TIME * 1000) + " curTimeStamp:" + curTimeStamp);
                if (timeStamp + EXPIRED_TIME * 1000 > curTimeStamp) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取设备码
     * 
     * @return
     */
    public static String getDeivceID() {
        DeviceInfoRealm deviceInfoRealm = new DeviceInfoRealm();
        RealmObject realmObject = RealmHelper.getInstance().queryRealmObjectByValue(deviceInfoRealm, "deviceKey", DeviceInfoRealm.DEVICEINFO_KEY);
        String deviceUniqueId = getDeviceUniqueId();
        if (!(realmObject instanceof DeviceInfoRealm)) {
            deviceInfoRealm.setDeviceID(deviceUniqueId);
            RealmHelper.getInstance().addRealmObject(deviceInfoRealm);
        } else if (TextUtils.isEmpty(((DeviceInfoRealm) realmObject).getDeviceID())) {
            deviceInfoRealm.setDeviceID(deviceUniqueId);
            RealmHelper.getInstance().updateRealmObject(deviceInfoRealm);
        } else {
            deviceUniqueId = ((DeviceInfoRealm) realmObject).getDeviceID();
        }
        RealmHelper.getInstance().close();
        LogHelper.releaseLog(TAG + "getDeivceID deviceUniqueId:" + deviceUniqueId);
        return deviceUniqueId;
    }

    /**
     * 用户是否已签到
     * 
     * @return
     */
    public static boolean userIsSign() {
        SignInfoRealm signInfoRealm = AccountHelper.getInstance().getUserSignInfo();
        if (null == signInfoRealm) {
            LogHelper.errorLog(TAG + "userIsSign signInfoRealm is null!");
            return false;
        }
        LogHelper.releaseLog(TAG + "userIsSign signInfoRealm:" + signInfoRealm.toString());
        long lastSignTimeStamp = signInfoRealm.getLastSignTimeStamp();
        long toDayZeroTimeStamp = getToDayZeroTimeStamp();
        long nextDayZeroTimeStamp = getNextDayZeroTimeStamp();
        if (lastSignTimeStamp >= toDayZeroTimeStamp && lastSignTimeStamp < nextDayZeroTimeStamp) {
            // 最后一次签到时间在今天零点和明天零点之间，表示今天已签到
            return true;
        }
        return false;
    }

    /**
     * 获取今天零点的时间戳
     * 
     * @return
     */
    public static long getToDayZeroTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        String toDayZeroTime = format.format(date);
        long toDayTimeStamp = getTimeStamp(toDayZeroTime, "");
        LogHelper.releaseLog(TAG + "getToDayZero toDayZeroTime:" + toDayZeroTime + " toDayTimeStamp:" + toDayTimeStamp);
        return toDayTimeStamp;
    }

    /**
     * 获取下一天零点的时间戳
     *
     * @return
     */
    public static long getNextDayZeroTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        String nextDayZeroTime = format.format(date);
        long nextDayTimeStamp = getTimeStamp(nextDayZeroTime, "");
        LogHelper.releaseLog(TAG + "getNextDayZero nextDayZeroTime:" + nextDayZeroTime + " nextDayTimeStamp:" + nextDayTimeStamp);
        return nextDayTimeStamp;
    }

    /**
     * 获取当前本地apk的版本
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        LogHelper.releaseLog(TAG + "getVersionCode");
        int versionCode = 0;
        try {
            // 获取软件版本号，android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getVersionCode Exception! msg:" + e.getMessage());
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        LogHelper.releaseLog(TAG + "getVersionName");
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getVersionName Exception! msg:" + e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取系统当前剩余的内存
     * 
     * @param context
     * @return
     */
    public static String getAvailMemoryInfo(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long size = outInfo.availMem;
        return getDataSize(size);
    }

    /**
     * 计算当前数据大小
     * 
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            // 0和#表示数字，但是#前面没有就用0表示
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            // 0和#表示数字，但是#前面没有就用0表示
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            // 0和#表示数字，但是#前面没有就用0表示
            return formater.format(gbsize) + "GB";
        } else {
            return "size:error";
        }
    }

    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取本地共享wifi数据
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static List<ShareWifiHttpData> getShareWifiJson(Context context, String fileName) {
        if (null != context) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                // 获取assets资源管理器
                AssetManager assetManager = context.getAssets();
                // 通过管理器打开文件并读取
                BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return parserJson(stringBuilder.toString());
        }
        return null;
    }

    /**
     * 解析本地共享wifi数据
     * 
     * @param str
     * @return
     */
    public static List<ShareWifiHttpData> parserJson(String str) {
        LogHelper.releaseLog(TAG + "parserJson str:" + str);
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                // 获取JSONObject中的数组数据
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Type listType = new TypeToken<List<ShareWifiHttpData>>() {
                }.getType();
                List<ShareWifiHttpData> list = new Gson().fromJson(jsonArray.toString(), listType);
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
