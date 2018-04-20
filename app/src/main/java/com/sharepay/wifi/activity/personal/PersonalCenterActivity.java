package com.sharepay.wifi.activity.personal;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.util.CommonUtil;

public class PersonalCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        PersonalCenterFragment mPersonalCenterFragment = (PersonalCenterFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_personal_center_container);
        if (CommonUtil.checkIsNull(mPersonalCenterFragment)) {
            mPersonalCenterFragment = PersonalCenterFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_personal_center_container, mPersonalCenterFragment);
            transaction.commit();
        }
        new PersonalCenterPresenter(mPersonalCenterFragment);
    }
}
