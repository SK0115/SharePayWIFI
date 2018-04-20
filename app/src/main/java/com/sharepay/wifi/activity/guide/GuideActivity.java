package com.sharepay.wifi.activity.guide;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.util.CommonUtil;

public class GuideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        GuideFragment guideFragment = (GuideFragment) getSupportFragmentManager().findFragmentById(R.id.fl_guide_container);
        if (CommonUtil.checkIsNull(guideFragment)) {
            guideFragment = GuideFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_guide_container, guideFragment);
            transaction.commit();
        }
        new GuidePresenter(guideFragment);
    }

}
