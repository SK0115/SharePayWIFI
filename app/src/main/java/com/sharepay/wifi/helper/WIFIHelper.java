package com.sharepay.wifi.helper;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.sharepay.wifi.R;
import com.sharepay.wifi.define.WIFIDefine;

import java.util.ArrayList;
import java.util.List;

public class WIFIHelper {

    private static final String TAG = "WIFIHelper ";

    /**
     * 获取当前正在连接的wifi的信息
     * 
     * @param context
     * @return
     */
    public static WifiInfo getCurrentConnectingWIFI(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            // 得到当前连接的wifi热点的信息
            if (null != wifiManager) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                LogHelper.releaseLog(TAG + "getCurrentConnectingWIFI wifiInfo:" + wifiInfo.toString());
                return wifiInfo;
            }
            return null;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getCurrentConnectingWIFI Exception! message:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取DhcpInfo
     * 
     * @param context
     * @return
     */
    public static DhcpInfo getDhcpInfo(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            // 得到当前连接的wifi热点的信息
            if (null != wifiManager) {
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                LogHelper.releaseLog(TAG + "getDhcpInfo dhcpInfo:" + dhcpInfo.toString());
                return dhcpInfo;
            }
            return null;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getDhcpInfo Exception! message:" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取当前正在连接的wifi的SSID
     * 
     * @param context
     * @return
     */
    public static String getCurrentConnectWIFISSID(Context context) {
        try {
            WifiInfo wifiInfo = getCurrentConnectingWIFI(context);
            if (null != wifiInfo) {
                String currentSSID = wifiInfo.getSSID();
                if (!TextUtils.isEmpty(currentSSID) && currentSSID.length() > 2) {
                    if (currentSSID.startsWith("\"") && currentSSID.endsWith("\"")) {
                        currentSSID = currentSSID.substring(1, currentSSID.length() - 1);
                    }
                    LogHelper.releaseLog(TAG + "getCurrentConnectWIFIName wifiName:" + currentSSID);
                    return currentSSID;
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getCurrentConnectWIFIName Exception! message:" + e.getMessage());
        }
        return "";
    }

    /**
     * 获取wifi的ssid
     * 
     * @param ssid
     * @return
     */
    public static String getWIFISSID(String ssid) {
        if (!TextUtils.isEmpty(ssid) && ssid.length() > 2) {
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        }
        LogHelper.releaseLog(TAG + "getWIFISSID wifiName:" + ssid);
        return ssid;
    }

    /**
     * 利用WifiConfiguration.KeyMgmt的管理机制，来判断当前wifi是否需要连接密码
     *
     * @return true：需要密码连接，false：不需要密码连接
     */
    public static boolean checkWifiHasPassword(Context context, String currentWifiSSID) {
        LogHelper.releaseLog(TAG + "checkIsCurrentWifiHasPassword currentSSID:" + currentWifiSSID);
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null == wifiManager) {
                return true;
            }
            // 得到当前WifiConfiguration列表，此列表包含所有已经连过的wifi的热点信息，未连过的热点不包含在此表中
            List<WifiConfiguration> wifiConfiguration = wifiManager.getConfiguredNetworks();
            if (!TextUtils.isEmpty(currentWifiSSID)) {
                if (wifiConfiguration != null && wifiConfiguration.size() > 0) {
                    for (WifiConfiguration configuration : wifiConfiguration) {
                        if (configuration != null && configuration.status == WifiConfiguration.Status.CURRENT) {
                            String ssid = null;
                            if (!TextUtils.isEmpty(configuration.SSID)) {
                                ssid = configuration.SSID;
                                if (configuration.SSID.startsWith("\"") && configuration.SSID.endsWith("\"")) {
                                    ssid = configuration.SSID.substring(1, configuration.SSID.length() - 1);
                                }
                            }
                            LogHelper.releaseLog(TAG + "checkIsCurrentWifiHasPassword currentSSID:" + currentWifiSSID + " ssid:" + ssid);
                            if (currentWifiSSID.equalsIgnoreCase(ssid)) {
                                // KeyMgmt.NONE表示无需密码
                                return (!configuration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "checkIsCurrentWifiHasPassword Exception! message:" + e.getMessage());
        }
        return true;
    }

    /**
     * 获取当前连接wifi的信号强度
     * 
     * @param context
     * @return
     */
    public static int getCurrentConnectWIFISignalStrength(Context context) {
        try {
            WifiInfo wifiInfo = getCurrentConnectingWIFI(context);
            if (null != wifiInfo) {
                LogHelper.releaseLog(TAG + "getCurrentConnectWIFISignalStrength Strength:" + wifiInfo.getRssi());
                return wifiInfo.getRssi();
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getCurrentConnectWIFISignalStrength Exception! message:" + e.getMessage());
        }
        return 0;
    }

    /**
     * 根据wifi信号强度获取对应的状态图
     * 
     * @param signalStrength
     * @return
     */
    public static int getWIFIStateImageBySignalStrength(int signalStrength) {
        LogHelper.releaseLog(TAG + "getWIFIStateImageBySignalStrength signalStrength:" + signalStrength);
        if (signalStrength <= WIFIDefine.WifiSignalStrength.HIGH_MAX && signalStrength >= WIFIDefine.WifiSignalStrength.HIGH_MIN) {
            return R.drawable.ic_common_wifi_3;
        } else if (signalStrength < WIFIDefine.WifiSignalStrength.HIGH_MIN && signalStrength >= WIFIDefine.WifiSignalStrength.MEDIUM_MIN) {
            return R.drawable.ic_common_wifi_2;
        } else if (signalStrength < WIFIDefine.WifiSignalStrength.MEDIUM_MIN) {
            return R.drawable.ic_common_wifi_1;
        }
        return R.drawable.ic_common_wifi_3;
    }

    /**
     * 根据wifi信号强度获取对应的文字
     * 
     * @param signalStrength
     * @return
     */
    public static int getWIFIStateStringBySignalStrength(int signalStrength) {
        LogHelper.releaseLog(TAG + "getWIFIStateStringBySignalStrength signalStrength:" + signalStrength);
        if (signalStrength <= WIFIDefine.WifiSignalStrength.HIGH_MAX && signalStrength >= WIFIDefine.WifiSignalStrength.HIGH_MIN) {
            return R.string.strong;
        } else if (signalStrength < WIFIDefine.WifiSignalStrength.HIGH_MIN && signalStrength >= WIFIDefine.WifiSignalStrength.MEDIUM_MIN) {
            return R.string.middle;
        } else if (signalStrength < WIFIDefine.WifiSignalStrength.MEDIUM_MIN) {
            return R.string.weak;
        }
        return R.string.strong;
    }

    /**
     * 扫描周围所有的wifi信息
     * 
     * @param context
     * @return
     */
    public static List<ScanResult> getAllWIFIList(Context context) {
        List<ScanResult> wifiList = new ArrayList<ScanResult>();
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null != wifiManager) {
                wifiList = wifiManager.getScanResults();
                LogHelper.releaseLog(TAG + "getAllWIFIList size：" + wifiList.size());
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getAllWIFIList Exception! message:" + e.getMessage());
        }
        return wifiList;
    }

    /**
     * 获取wifi开关的状态
     * 
     * @return
     */
    public static int getWIFISwitchState(Context context) {
        int state = WifiManager.WIFI_STATE_DISABLED;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null != wifiManager) {
                state = wifiManager.getWifiState();
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "getWIFISwitchState Exception! message:" + e.getMessage());
        }
        LogHelper.releaseLog(TAG + "getWIFISwitchState state:" + state);
        return state;
    }

    /**
     * 打开/关闭wifi开关
     * 
     * @param context
     * @param isOpen
     *            true 打开 false 关闭
     */
    public static void setWIFIEnabled(Context context, boolean isOpen) {
        LogHelper.releaseLog(TAG + "setWIFIEnabled! isOpen:" + isOpen);
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null != wifiManager) {
                wifiManager.setWifiEnabled(isOpen);
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "setWIFIEnabled Exception! message:" + e.getMessage());
        }
    }

    /**
     * 断开当前正在连接的wifi
     * 
     * @param context
     */
    public static void disconnectWIFI(Context context) {
        LogHelper.releaseLog(TAG + "disconnectWIFI!");
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (null != wifiManager) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (null != wifiInfo) {
                    int netId = wifiInfo.getNetworkId();
                    wifiManager.disableNetwork(netId);
                    wifiManager.disconnect();
                }
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "disconnectWIFI Exception! message:" + e.getMessage());
        }
    }
}
