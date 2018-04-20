package com.sharepay.wifi.activity.cost;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sharepay.wifi.R;
import com.sharepay.wifi.adapter.CostHistoryListAdapter;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.model.CostHistoryInfo;
import com.sharepay.wifi.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CostHistoryFragment extends BaseFragment implements CostHistoryContract.View {

    private CostHistoryActivity mActivity;
    private CostHistoryListAdapter mAdapter;

    @BindView(R.id.recyclerview_costhistory)
    RecyclerView recyclerviewCostHistory;

    public static CostHistoryFragment getInstance() {
        CostHistoryFragment mPersonalCenterFragment = new CostHistoryFragment();
        Bundle bundle = new Bundle();
        mPersonalCenterFragment.setArguments(bundle);
        return mPersonalCenterFragment;
    }

    @OnClick({ R.id.iv_cost_history_back })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_cost_history_back:
            mActivity.finish();
            break;
        default:
            break;
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_cost_history;
    }

    @Override
    protected void initView() {
        initCostHistory();
    }

    @Override
    protected void initData() {
        mActivity = (CostHistoryActivity) getActivity();
    }

    @Override
    public void setPresenter(CostHistoryContract.Presenter presenter) {
    }

    private void initCostHistory() {
        List<CostHistoryInfo> datas = new ArrayList<>();
        datas.add(new CostHistoryInfo("共享WIFI[123]", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("共享WIFI[456]", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("共享WIFI[789]", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("余额兑换网络时长 2小时", "2017-12-03 12:02:48", "- 2"));
        datas.add(new CostHistoryInfo("新用户分享红包", "2017-12-03 12:02:48", "+ 5"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        datas.add(new CostHistoryInfo("每日签到", "2017-12-03 12:02:48", "+ 2"));
        mAdapter = new CostHistoryListAdapter(mActivity, datas, false);
        recyclerviewCostHistory.setNestedScrollingEnabled(false);
        // 设置布局管理器
        recyclerviewCostHistory.setLayoutManager(new FullyLinearLayoutManager(mActivity));
        recyclerviewCostHistory.setAdapter(mAdapter);
    }
}
