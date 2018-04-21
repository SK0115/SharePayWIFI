package com.sharepay.wifi.activity.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.module.login.LoginFragment;
import com.sharepay.wifi.module.login.LoginPresenter;
import com.sharepay.wifi.util.CommonUtil;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.fl_login_container);
        if (CommonUtil.checkIsNull(loginFragment)) {
            loginFragment = LoginFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_login_container, loginFragment);
            transaction.commit();
        }
        new LoginPresenter(loginFragment);
    }
}
