package com.sharepay.wifi.module.wifiShare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.wifiShare.WifiShareActivity;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.baseCtrl.MEditText;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.helper.LocationHelper;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.helper.WIFIHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.info.IncomeInfo;
import com.sharepay.wifi.model.info.WIFIShareInfo;
import com.sharepay.wifi.model.realm.AccountInfoRealm;
import com.sharepay.wifi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WifiShareFragment extends BaseFragment implements WifiShareContract.View {

    private final String TAG = "WifiShareFragment ";
    private WifiShareActivity mActivity;
    private WifiShareContract.Presenter mPresenter;
    private List<IncomeInfo> mIncomeInfoList;
    private int mCurShowIncome = 0;
    private LocationHelper mLocationHelper;
    private double mXCoordinate = 0; // 纬度
    private double mYCoordinate = 0; // 经度
    private String mWifiName; // wifi名字

    @BindView(R.id.tv_wifi_share_income)
    TextView mIncomeTextView;
    @BindView(R.id.tv_wifi_share_main_title)
    TextView mTitleView;
    @BindView(R.id.wifi_share_pass_et)
    MEditText mPassView;

    public static WifiShareFragment getInstance() {
        WifiShareFragment mPersonalCenterFragment = new WifiShareFragment();
        Bundle bundle = new Bundle();
        mPersonalCenterFragment.setArguments(bundle);
        return mPersonalCenterFragment;
    }

    @OnClick({ R.id.wifi_share_pass_et, R.id.iv_wifi_share_back, R.id.iv_wifi_share_left, R.id.iv_wifi_share_right, R.id.tv_wifi_share_share })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.wifi_share_pass_et:
            mPassView.setCursorVisible(true);
            break;
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
        mPresenter = presenter;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_wifi_share;
    }

    @Override
    protected void initView() {
        Intent intent = mActivity.getIntent();
        if (null != intent) {
            mWifiName = intent.getStringExtra("title");
            LogHelper.releaseLog(TAG + "initView title:" + mWifiName);
            mTitleView.setText(mWifiName);
        }
        initIncomeView();
        List<String> permissionsList = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.releaseLog(TAG + "initView no location permission!");
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    WIFIDefine.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            LogHelper.releaseLog(TAG + "initView has location permission!");
            startLocation();
        }
    }

    @Override
    public void setWifiShareResult(BaseHttpResult<BaseHttpData> httpResult) {
        if (null != httpResult && WIFIDefine.HttpResultState.SUCCESS.equals(httpResult.getStatus())) {
            ToastUtils.showShort(getResources().getString(R.string.wifi_share_info_success));
            mActivity.finish();
        } else {
            ToastUtils.showShort(getResources().getString(R.string.wifi_share_info_fail));
        }
    }

    @Override
    protected void initData() {
        mActivity = (WifiShareActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocationHelper) {
            mLocationHelper.setLocationCallBack(null);
            mLocationHelper.release();
            mLocationHelper = null;
        }
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
            AccountInfoRealm accountInfoRealm = AccountHelper.getInstance().getAccountInfo();
            if (null == accountInfoRealm || TextUtils.isEmpty(accountInfoRealm.getMobile())) {
                ToastUtils.showShort(getResources().getString(R.string.please_login));
            } else {
                String integration = mIncomeInfoList.get(mCurShowIncome).getIntegration();
                WIFIShareInfo wifiShareInfo = new WIFIShareInfo();
                // 手机号
                wifiShareInfo.setMobile(accountInfoRealm.getMobile());

                // wifi名字
                if (TextUtils.isEmpty(mWifiName)) {
                    ToastUtils.showShort(getResources().getString(R.string.wifi_share_name_null));
                    return;
                }
                wifiShareInfo.setName(mWifiName);

                // wifi密码
                String pass = mPassView.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    ToastUtils.showShort(getResources().getString(R.string.wifi_share_info_pass_null));
                    return;
                }
                wifiShareInfo.setPass(pass);

                // ip和网关
                WifiInfo wifiInfo = WIFIHelper.getCurrentConnectingWIFI(mActivity);
                if (null == wifiInfo) {
                    ToastUtils.showShort(getResources().getString(R.string.wifi_share_info_null));
                    return;
                }
                wifiShareInfo.setMac(wifiInfo.getBSSID());
                wifiShareInfo.setIp(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                DhcpInfo dhcpInfo = WIFIHelper.getDhcpInfo(mActivity);
                if (null != dhcpInfo) {
                    wifiShareInfo.setGateway(Formatter.formatIpAddress(dhcpInfo.gateway));
                }

                // 经纬度
                try {
                    if (mXCoordinate <= 0 || mYCoordinate <= 0) {
                        ToastUtils.showShort(getResources().getString(R.string.wifi_share_location_fail));
                        return;
                    }
                    wifiShareInfo.setXCoordinate(String.valueOf(mXCoordinate));
                    wifiShareInfo.setYCoordinate(String.valueOf(mYCoordinate));
                } catch (Exception e) {
                    LogHelper.errorLog(TAG + "shareWifi Exception! msg:" + e.getMessage());
                }

                // 共享收益
                if (TextUtils.isEmpty(integration)) {
                    ToastUtils.showShort(getResources().getString(R.string.wifi_share_select_income));
                    return;
                }
                wifiShareInfo.setEarnings(integration);
                LogHelper.releaseLog(TAG + "shareWifi wifiShareInfo:" + wifiShareInfo.toString());
                if (null != mPresenter) {
                    mPresenter.requestUserShareWifi(wifiShareInfo);
                }
            }
        }
    }

    public void startLocation() {
        if (null == mLocationHelper) {
            mLocationHelper = new LocationHelper();
        }
        mLocationHelper.setLocationCallBack(mLocationCallBack);
        mLocationHelper.location(mActivity);
    }

    private WIFIDefine.LocationCallBack mLocationCallBack = new WIFIDefine.LocationCallBack() {
        @Override
        public void setLocation(Location location) {
            if (null != location) {
                LogHelper.releaseLog(TAG + "LocationCallBack setLocation" + " latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
                mXCoordinate = location.getLatitude();
                mYCoordinate = location.getLongitude();
            }
        }
    };
}
