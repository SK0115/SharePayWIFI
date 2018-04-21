package com.sharepay.wifi.helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.sharepay.wifi.model.info.WIFIInfo;

import java.util.List;

public class WIFIConnectManager {

    private static final String TAG = "WIFIConnectManager ";
    private static final String WIFICONNECT_OPEN_MSG = "openWifiMsg";
    private static final String WIFICONNECT_CONFIGERROR_MSG = "configErrorMsg";
    private static final String WIFICONNECT_ENABLE_SUCCESS = "enableNetworkSuccess";
    private static final String WIFICONNECT_ENABLE_FAIL = "enableNetworkFail";
    private static final String WIFICONNECT_CONNECT_SUCCESS = "connectSuccess";
    private static final String WIFICONNECT_CONNECT_FAIL = "connectFail";
    private static final String WIFICONNECT_ERROR = "connectError";

    private WifiManager mWifiManager;
    public Handler mHandler;

    /**
     * 向UI发送消息
     * 
     * @param info
     *            消息
     */
    private void sendMsg(String info) {
        if (null != mHandler) {
            Message msg = new Message();
            msg.obj = info;
            mHandler.sendMessage(msg);// 向Handler发送消息
        } else {
            LogHelper.errorLog(TAG + "sendMsg is error info:" + info);
        }
    }

    // WIFICIPHER_WEP是WEP ，WIFICIPHER_WPA是WPA，WIFICIPHER_NOPASS没有密码
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public WIFIConnectManager(Context context) {
        try {
            mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "WIFIConnectManager Exception! error:" + e.getMessage());
        }
    }

    /**
     * 提供一个外部接口，传入要连接的无线网
     * 
     * @param ssid
     * @param password
     * @param type
     */
    public void connectWIFI(String ssid, String password, WifiCipherType type) {
        LogHelper.releaseLog(TAG + "connectWIFI ssid:" + ssid + " password:" + password + " type:" + type);
        Thread thread = new Thread(new ConnectRunnable(ssid, password, type));
        thread.start();
    }

    /**
     * 已有配置链接
     *
     * @param wifiInfo
     * @return
     */
    public boolean connectExistWIFI(WIFIInfo wifiInfo) {
        if (!openWifi()) {
            return false;
        }
        try {
            // 得到当前连接的wifi热点的信息
            if (null != mWifiManager) {
                List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
                if (null == existingConfigs || existingConfigs.size() <= 0) {
                    return false;
                }
                WifiConfiguration wifiConfiguration = null;
                for (int i = 0; i < existingConfigs.size(); i++) {
                    WifiConfiguration config = existingConfigs.get(i);
                    if (null != config && null != wifiInfo && TextUtils.equals(WIFIHelper.getWIFISSID(config.SSID), wifiInfo.getName())
                            && TextUtils.equals(config.BSSID, wifiInfo.getBSSID())) {
                        wifiConfiguration = config;
                        break;
                    }
                }
                while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    // 为了避免程序一直while循环，睡100毫秒再检测
                    Thread.currentThread();
                    Thread.sleep(100);
                }
                boolean bRet = false;
                if (null != wifiConfiguration) {
                    bRet = mWifiManager.enableNetwork(wifiConfiguration.networkId, true);
                    mWifiManager.saveConfiguration();
                }
                return bRet;
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "connectExistWIFI Exception! error:" + e.getMessage());
        }
        return false;
    }

    /**
     * 查看以前是否也配置过这个网络
     * 
     * @param ssid
     * @return
     */
    private WifiConfiguration isExsits(String ssid) {
        LogHelper.releaseLog(TAG + "isExsits ssid:" + ssid);
        if (null != mWifiManager) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + ssid + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }

    private WifiConfiguration createWifiInfo(String ssid, String password, WifiCipherType type) {
        LogHelper.releaseLog(TAG + "connectWIFI ssid:" + ssid + " password:" + password + " type:" + type);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        // nopass
        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        // wep
        if (type == WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(password)) {
                if (isHexWepKey(password)) {
                    config.wepKeys[0] = password;
                } else {
                    config.wepKeys[0] = "\"" + password + "\"";
                }
            }
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wpa
        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    // 打开wifi功能
    private boolean openWifi() {
        boolean bRet = true;
        if (null != mWifiManager && !mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
        }
        LogHelper.releaseLog(TAG + "openWifi:" + bRet);
        return bRet;
    }

    class ConnectRunnable implements Runnable {

        private String ssid;
        private String password;
        private WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                // 打开wifi
                openWifi();
                sendMsg(WIFICONNECT_OPEN_MSG);
                Thread.sleep(200);
                // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
                // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
                while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    try {
                        // 为了避免程序一直while循环，让它睡个100毫秒检测...
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }

                WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
                if (null == wifiConfig) {
                    sendMsg(WIFICONNECT_CONFIGERROR_MSG);
                    return;
                }

                WifiConfiguration tempConfig = isExsits(ssid);

                if (null != tempConfig) {
                    mWifiManager.removeNetwork(tempConfig.networkId);
                }
                int netID = mWifiManager.addNetwork(wifiConfig);
                boolean enabled = mWifiManager.enableNetwork(netID, true);
                sendMsg(enabled ? WIFICONNECT_ENABLE_SUCCESS : WIFICONNECT_ENABLE_FAIL);
                boolean connected = mWifiManager.reconnect();
                sendMsg(connected ? WIFICONNECT_CONNECT_SUCCESS : WIFICONNECT_CONNECT_FAIL);
            } catch (Exception e) {
                sendMsg(WIFICONNECT_ERROR);
                LogHelper.errorLog(TAG + " ConnectRunnable run Exception! error:" + e.getMessage());
            }
        }
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();
        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }
        return true;
    }
}
