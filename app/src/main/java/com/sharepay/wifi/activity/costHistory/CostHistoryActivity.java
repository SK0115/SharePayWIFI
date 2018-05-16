package com.sharepay.wifi.activity.costHistory;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.module.costHistory.CostHistoryFragment;
import com.sharepay.wifi.module.costHistory.CostHistoryPresenter;
import com.sharepay.wifi.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

public class CostHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_history);

        CostHistoryFragment costHistoryFragment = (CostHistoryFragment) getSupportFragmentManager().findFragmentById(R.id.fl_cost_history_container);
        if (CommonUtil.checkIsNull(costHistoryFragment)) {
            costHistoryFragment = CostHistoryFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_cost_history_container, costHistoryFragment);
            transaction.commit();
        }
        new CostHistoryPresenter(costHistoryFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CostHistoryActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CostHistoryActivity");
    }
}
