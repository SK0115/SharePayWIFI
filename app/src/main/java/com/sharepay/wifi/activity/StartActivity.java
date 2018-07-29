package com.sharepay.wifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sharepay.wifi.R;
import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.activity.guide.GuideActivity;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.main.MainActivity;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.helper.RealmHelper;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.http.StartRequestService;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.TokenHttpData;
import com.sharepay.wifi.model.realm.AccountInfoRealm;
import com.sharepay.wifi.util.CommonUtil;
import com.sharepay.wifi.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class StartActivity extends BaseActivity {
    private final String TAG = "StartActivity ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

        HttpRequestHelper.getInstance().requestToken(new BaseHttpObserver<BaseHttpResult<TokenHttpData>>(new WIFIDefine.HttpRequestCallBack() {
            @Override
            public void onNext(Object tokenHttpData) {
                if (tokenHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestToken onNext! tokenData:" + tokenHttpData.toString());
                    TokenHttpData tokenData = ((BaseHttpResult<TokenHttpData>) tokenHttpData).getHttpData();
                    if (null != tokenData) {
                        CommonUtil.saveToken(tokenData);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestToken onError! msg:" + e.getMessage());
            }
        }), HttpRequestHelper.getInstance().create(StartRequestService.class));

        // boolean isShow =
        // PreferenceUtil.getInstance().getBooleanValue(WIFIDefine.KEY_PREFERENCE_ISSHOWSTARTOVER,
        // false);
        boolean isShow = true;
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("StartActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("StartActivity");
    }

    @Override
    protected void setStatusBar() {
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
