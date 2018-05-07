package com.sharepay.wifi.activity.personalCenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.module.personalCenter.PersonalCenterFragment;
import com.sharepay.wifi.module.personalCenter.PersonalCenterPresenter;
import com.sharepay.wifi.util.CommonUtil;

public class PersonalCenterActivity extends BaseActivity {

    private PersonalCenterFragment mPersonalCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        mPersonalCenterFragment = (PersonalCenterFragment) getSupportFragmentManager().findFragmentById(R.id.fl_personal_center_container);
        if (CommonUtil.checkIsNull(mPersonalCenterFragment)) {
            mPersonalCenterFragment = PersonalCenterFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_personal_center_container, mPersonalCenterFragment);
            transaction.commit();
        }
        new PersonalCenterPresenter(mPersonalCenterFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        case WIFIDefine.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSIONS:
            if (permissions.length == 1 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPersonalCenterFragment.downloadApk();
            }
            break;
        }
    }
}
