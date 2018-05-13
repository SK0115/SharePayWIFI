package com.sharepay.wifi.activity.wifiDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.module.wifiDetail.WifiDetailFragment;
import com.sharepay.wifi.module.wifiDetail.WifiDetailPresenter;
import com.sharepay.wifi.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

public class WifiDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_detail);

        WifiDetailFragment mWifiDetailFragment = (WifiDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_wifi_detail_container);
        if (CommonUtil.checkIsNull(mWifiDetailFragment)) {
            mWifiDetailFragment = WifiDetailFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_wifi_detail_container, mWifiDetailFragment);
            transaction.commit();
        }
        new WifiDetailPresenter(mWifiDetailFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WifiDetailActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WifiDetailActivity");
    }
}
