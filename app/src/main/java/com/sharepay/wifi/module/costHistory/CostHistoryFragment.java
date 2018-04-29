package com.sharepay.wifi.module.costHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.costHistory.CostHistoryActivity;
import com.sharepay.wifi.adapter.CostHistoryListAdapter;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.base.OnLoadMoreListener;
import com.sharepay.wifi.baseCtrl.FullyLinearLayoutManager;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;
import com.sharepay.wifi.model.info.CostHistoryInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CostHistoryFragment extends BaseFragment implements CostHistoryContract.View {

    private final String TAG = "CostHistoryFragment ";
    private CostHistoryContract.Presenter mPresenter;
    private CostHistoryActivity mActivity;
    private CostHistoryListAdapter mAdapter;
    private String mPhoneNum;
    private int mRequestPageNo = 0;
    private boolean mIsLoadFinish = false; // 是否加载结束

    @BindView(R.id.recyclerview_costhistory)
    RecyclerView mCostHistoryRecyclerview;

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
        Intent intent = mActivity.getIntent();
        mPhoneNum = intent.getStringExtra("phoneNum");
        LogHelper.releaseLog(TAG + "initView mPhoneNum:" + mPhoneNum);
        if (!TextUtils.isEmpty(mPhoneNum)) {
            mPresenter.requestUserIntegralHistory(mPhoneNum, mRequestPageNo);
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
        if (null != mAdapter) {
            mAdapter.setLoadingViewVisibility(false);
        }
        if (null != userIntegralHistoryHttpDataList && userIntegralHistoryHttpDataList.size() > 0) {
            List<CostHistoryInfo> datas = new ArrayList<>();
            for (int i = 0; i < userIntegralHistoryHttpDataList.size(); i++) {
                UserIntegralHistoryHttpData data = userIntegralHistoryHttpDataList.get(i);
                CostHistoryInfo info = new CostHistoryInfo();
                String content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN_TEXT;
                String integration = "";
                if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SIGN_TEXT;
                    integration = "+" + data.getIntegral();
                } else if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SHARE.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SHARE_TEXT;
                    integration = "+" + data.getIntegral();
                } else if (WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SPENDING.equals(data.getType())) {
                    content = WIFIDefine.USER_INTEGRAL_HISTORY_TYPE.TYPE_SPENDING_TEXT;
                    integration = "" + data.getIntegral();
                }
                info.setContent(content);
                info.setTime(data.getAddTime());
                info.setIntegration(integration);
                datas.add(info);
            }
            LogHelper.releaseLog(TAG + "setUserIntegralHistoryHttpResult size:" + datas.size());

            if (null == mAdapter) {
                mAdapter = new CostHistoryListAdapter(mActivity, datas, true);
                mAdapter.setLoadingView(R.layout.cost_history_loading_view);
                mAdapter.setOnLoadMoreListener(mLoadMoreListener);
                mCostHistoryRecyclerview.setNestedScrollingEnabled(false);
                // 设置布局管理器
                mCostHistoryRecyclerview.setLayoutManager(new FullyLinearLayoutManager(mActivity));
                mCostHistoryRecyclerview.setAdapter(mAdapter);
            } else {
                mAdapter.setLoadMoreData(datas);
            }
        } else {
            mIsLoadFinish = true;
        }
    }

    private OnLoadMoreListener mLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(boolean isReload) {
            LogHelper.releaseLog(TAG + "OnLoadMoreListener onLoadMore isReload:" + isReload);
            if (!isReload && !mIsLoadFinish) {
                if (null != mAdapter) {
                    mAdapter.setLoadingViewVisibility(true);
                }
                mRequestPageNo++;
                mPresenter.requestUserIntegralHistory(mPhoneNum, mRequestPageNo);
            }
        }
    };
}
