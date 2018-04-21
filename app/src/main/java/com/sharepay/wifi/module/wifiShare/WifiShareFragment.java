package com.sharepay.wifi.module.wifiShare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.wifiShare.WifiShareActivity;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.helper.LocationHelper;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.model.IncomeInfo;
import com.sharepay.wifi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WifiShareFragment extends BaseFragment implements WifiShareContract.View {

    private final String TAG = "WifiShareFragment ";
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private WifiShareActivity mActivity;
    private List<IncomeInfo> mIncomeInfoList;
    private int mCurShowIncome = 0;
    private LocationHelper mLocationHelper;

    @BindView(R.id.tv_wifi_share_income)
    TextView mIncomeTextView;
    @BindView(R.id.tv_wifi_share_main_title)
    TextView mTitleView;

    public static WifiShareFragment getInstance() {
        WifiShareFragment mPersonalCenterFragment = new WifiShareFragment();
        Bundle bundle = new Bundle();
        mPersonalCenterFragment.setArguments(bundle);
        return mPersonalCenterFragment;
    }

    @OnClick({ R.id.iv_wifi_share_back, R.id.iv_wifi_share_left, R.id.iv_wifi_share_right, R.id.tv_wifi_share_share })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_wifi_share_back:
            mActivity.finish();
            break;
        case R.id.iv_wifi_share_left:
            switchIncomeContent(true);
            break;
        case R.id.iv_wifi_share_right:
            switchIncomeContent(false);
            break;
        case R.id.tv_wifi_share_share:
            shareWifi();
            break;
        default:
            break;
        }
    }

    @Override
    public void setPresenter(WifiShareContract.Presenter presenter) {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_wifi_share;
    }

    @Override
    protected void initView() {
        Intent intent = mActivity.getIntent();
        if (null != intent) {
            LogHelper.releaseLog("WifiShareFragment initView title:" + intent.getStringExtra("title"));
            mTitleView.setText(intent.getStringExtra("title"));
        }
        initIncomeView();
    }

    @Override
    protected void initData() {
        mActivity = (WifiShareActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationHelper = null;
    }

    private void initIncomeView() {
        mIncomeInfoList = new ArrayList<IncomeInfo>();
        mIncomeInfoList.add(new IncomeInfo(mActivity.getResources().getString(R.string.one_intergration_hour), "1"));
        mIncomeInfoList.add(new IncomeInfo(mActivity.getResources().getString(R.string.two_intergration_hour), "2"));
        mIncomeInfoList.add(new IncomeInfo(mActivity.getResources().getString(R.string.three_intergration_hour), "3"));
        mIncomeInfoList.add(new IncomeInfo(mActivity.getResources().getString(R.string.four_intergration_hour), "4"));
        mIncomeInfoList.add(new IncomeInfo(mActivity.getResources().getString(R.string.five_intergration_hour), "5"));
    }

    private void switchIncomeContent(boolean isLeft) {
        if (null == mIncomeInfoList || mIncomeInfoList.size() <= 0) {
            return;
        }
        int curShowIncome = mCurShowIncome;
        if (isLeft) {
            mCurShowIncome--;
        } else {
            mCurShowIncome++;
        }
        if (mCurShowIncome <= 0) {
            mCurShowIncome = 0;
        }
        if (mCurShowIncome >= mIncomeInfoList.size()) {
            mCurShowIncome = mIncomeInfoList.size() - 1;
        }
        if (mCurShowIncome != curShowIncome) {
            mIncomeTextView.setText(mIncomeInfoList.get(mCurShowIncome).getContent());
        }
    }

    private void shareWifi() {
        if (null != mIncomeInfoList && mIncomeInfoList.size() > 0 && mCurShowIncome < mIncomeInfoList.size()) {
            String integration = mIncomeInfoList.get(mCurShowIncome).getIntegration();
            ToastUtils.showShort("您的收益积分：" + integration);
            List<String> permissionsList = new ArrayList<String>();
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                LogHelper.releaseLog(TAG + "no location permission!");
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                LogHelper.releaseLog(TAG + "has location permission!");
                startLocation();
            }
        }
    }

    public void startLocation() {
        if (null == mLocationHelper) {
            mLocationHelper = new LocationHelper(mLocationCallBack);
        }
        mLocationHelper.location(mActivity);
    }

    private LocationCallBack mLocationCallBack = new LocationCallBack() {
        @Override
        public void setLocation(Location location) {
            LogHelper.releaseLog(TAG + "setLocation" + " latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
        }
    };

    public interface LocationCallBack {
        void setLocation(Location location);
    }
}
