package com.sharepay.wifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sharepay.wifi.R;
import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.activity.guide.GuideActivity;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.main.MainActivity;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.RealmHelper;
import com.sharepay.wifi.model.AccountInfoRealm;
import com.sharepay.wifi.util.CommonUtil;
import com.sharepay.wifi.util.PreferenceUtil;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        CommonUtil.getDeivceID();

        boolean isShow = PreferenceUtil.getInstance().getBooleanValue(WIFIDefine.KEY_PREFERENCE_ISSHOWSTARTOVER, false);
        if (!isShow) {
            PreferenceUtil.getInstance().saveBooleanValue(WIFIDefine.KEY_PREFERENCE_ISSHOWSTARTOVER, true);
            startDelay(GuideActivity.class);
        } else {
            AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
            if (RealmHelper.getInstance().isRealmObjectExist(accountInfoRealm, "loginKey", AccountInfoRealm.LOGIN_KEY)) {
                startDelay(MainActivity.class);
            } else {
                startDelay(LoginActivity.class);
            }
        }
    }

    private void startDelay(final Class cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, cls);
                startActivity(intent);
                SPApplication.clearLastActivity();
                finish();
            }
        }, 2000);
    }
}
