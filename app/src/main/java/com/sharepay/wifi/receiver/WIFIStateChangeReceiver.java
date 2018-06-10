package com.sharepay.wifi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.sharepay.wifi.helper.LogHelper;

/**
 * wifi开关变化广播
 */
public class WIFIStateChangeReceiver extends BroadcastReceiver {

    private WIFIStateChangeListener mWIFIStateChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            String action = intent.getAction();
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            LogHelper.releaseLog("WIFIStateChangeReceiver action:" + action + " wifiState:" + wifiState);
            if (TextUtils.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                if (wifiState == WifiManager.WIFI_STATE_ENABLED && null != mWIFIStateChangeListener) {
                    // WIFI开关处于打开状态
                    mWIFIStateChangeListener.wifiStateOpen(true);
                } else {
                    // WIFI开关处于关闭状态
                    mWIFIStateChangeListener.wifiStateOpen(false);
                }
            }
        }
    }

    public void setWIFIStateChangeListener(WIFIStateChangeListener listener) {
        mWIFIStateChangeListener = listener;
    }

    public interface WIFIStateChangeListener {
        void wifiStateOpen(boolean isOpen);
    }
}
