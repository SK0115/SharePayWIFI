package com.sharepay.wifi.activity.cost;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.util.CommonUtil;

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
}
