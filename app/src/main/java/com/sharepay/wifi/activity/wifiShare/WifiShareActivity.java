package com.sharepay.wifi.activity.wifiShare;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.module.wifiShare.WifiShareFragment;
import com.sharepay.wifi.module.wifiShare.WifiSharePresenter;
import com.sharepay.wifi.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

public class WifiShareActivity extends BaseActivity {

    private final String TAG = "WifiShareActivity ";

    private WifiShareFragment mWifiShareFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_share);

        mWifiShareFragment = (WifiShareFragment) getSupportFragmentManager().findFragmentById(R.id.fl_wifi_share_container);
        if (CommonUtil.checkIsNull(mWifiShareFragment)) {
            mWifiShareFragment = WifiShareFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_wifi_share_container, mWifiShareFragment);
            transaction.commit();
        }
        new WifiSharePresenter(mWifiShareFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WifiShareActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WifiShareActivity");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        case WIFIDefine.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            if ((permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) || (permissions.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // 获取到获取位置权限
                if (null != mWifiShareFragment) {
                    mWifiShareFragment.startLocation();
                }
            }
            break;
        }
    }
}
