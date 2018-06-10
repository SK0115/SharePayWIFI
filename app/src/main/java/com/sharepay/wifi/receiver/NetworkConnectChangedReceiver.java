package com.sharepay.wifi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.sharepay.wifi.helper.LogHelper;

/**
 * 网络连接状态变化广播
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private NetworkConnectChangedListener mNetworkConnectChangedListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra instanceof NetworkInfo && null != mNetworkConnectChangedListener) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                LogHelper.releaseLog("NetConnectReceiver state:" + state);
                if (state == NetworkInfo.State.CONNECTED) {
                    mNetworkConnectChangedListener.networkConnected();
                }
            }
        }
    }

    public void setNetworkConnectChangedListener(NetworkConnectChangedListener listener) {
        mNetworkConnectChangedListener = listener;
    }

    public interface NetworkConnectChangedListener {
        void networkConnected();
    }
}
