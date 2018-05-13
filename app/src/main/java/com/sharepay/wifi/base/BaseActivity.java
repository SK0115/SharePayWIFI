package com.sharepay.wifi.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharepay.wifi.R;
import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.util.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPApplication.addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_login_bg));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPApplication.clearLastActivity();
    }
}
