package com.sharepay.wifi.module.costHistory;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.costHistory.CostHistoryActivity;
import com.sharepay.wifi.adapter.CostHistoryListAdapter;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.baseCtrl.FullyLinearLayoutManager;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;
import com.sharepay.wifi.model.info.CostHistoryInfo;
import com.sharepay.wifi.model.realm.AccountInfoRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CostHistoryFragment extends BaseFragment implements CostHistoryContract.View {

    private CostHistoryContract.Presenter mPresenter;
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
        AccountInfoRealm accountInfoRealm = AccountHelper.getInstance().getAccountInfo();
        if (null != accountInfoRealm && !TextUtils.isEmpty(accountInfoRealm.getMobile()) && !TextUtils.isEmpty(accountInfoRealm.getId())) {
            mPresenter.requestUserIntegralHistory(accountInfoRealm.getMobile(), 1);
        }
    }

    @Override
    protected void initData() {
        mActivity = (CostHistoryActivity) getActivity();
    }

    @Override
    public void setPresenter(CostHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setUserIntegralHistoryHttpResult(List<UserIntegralHistoryHttpData> userIntegralHistoryHttpDataList, int pageNo) {
        if (null != userIntegralHistoryHttpDataList && userIntegralHistoryHttpDataList.size() > 0) {
            List<CostHistoryInfo> datas = new ArrayList<>();
            for (int i = 0; i < userIntegralHistoryHttpDataList.size(); i++) {
                UserIntegralHistoryHttpData data = userIntegralHistoryHttpDataList.get(i);
                CostHistoryInfo info = new CostHistoryInfo();
                String content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN_TEXT;
                String integration = "";
                if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN_TEXT;
                    integration = "+ " + data.getIntegral();
                } else if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SHARE.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SHARE_TEXT;
                    integration = "+ " + data.getIntegral();
                } else if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SPENDING.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SPENDING_TEXT;
                    integration = "- " + data.getIntegral();
                }
                info.setContent(content);
                info.setTime(data.getAddTime());
                info.setIntegration(integration);
                datas.add(info);
            }
            mAdapter = new CostHistoryListAdapter(mActivity, datas, false);
            recyclerviewCostHistory.setNestedScrollingEnabled(false);
            // 设置布局管理器
            recyclerviewCostHistory.setLayoutManager(new FullyLinearLayoutManager(mActivity));
            recyclerviewCostHistory.setAdapter(mAdapter);
        }
    }
}
