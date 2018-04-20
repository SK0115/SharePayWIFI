package com.sharepay.wifi.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sharepay.wifi.SPApplication;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPApplication.clearLastActivity();
    }
}
