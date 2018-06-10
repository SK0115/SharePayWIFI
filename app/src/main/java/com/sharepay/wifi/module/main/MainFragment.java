package com.sharepay.wifi.module.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.main.MainActivity;
import com.sharepay.wifi.activity.personalCenter.PersonalCenterActivity;
import com.sharepay.wifi.activity.wifiDetail.WifiDetailActivity;
import com.sharepay.wifi.activity.wifiShare.WifiShareActivity;
import com.sharepay.wifi.adapter.MainWifiListAdapter;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.base.OnBaseItemClickListener;
import com.sharepay.wifi.baseCtrl.FullyLinearLayoutManager;
import com.sharepay.wifi.baseCtrl.ProgressView;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.helper.BaseTimer;
import com.sharepay.wifi.helper.LocationHelper;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.helper.WIFIConnectManager;
import com.sharepay.wifi.helper.WIFIHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.ShareWifiHttpData;
import com.sharepay.wifi.model.info.WIFIInfo;
import com.sharepay.wifi.model.info.WIFIShareInfo;
import com.sharepay.wifi.model.realm.AccountInfoRealm;
import com.sharepay.wifi.model.realm.CurrentWifiInfoRealm;
import com.sharepay.wifi.util.CommonUtil;
import com.sharepay.wifi.util.DialogUtils;
import com.sharepay.wifi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements MainContract.View {

    private static final String TAG = "MainFragment ";
    private static final int SCAN_WIFI_TIME = 30 * 1000;
    private static final int HIDE_LOADING_TIME = 5 * 1000;

    @BindView(R.id.iv_main_share)
    ImageView mShareView;
    @BindView(R.id.layout_main_connect_wifi)
    RelativeLayout mConnectWifiLayout;
    @BindView(R.id.recyclerview_main)
    RecyclerView recyclerviewMain;
    @BindView(R.id.layout_tips)
    RelativeLayout layoutTips;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.tv_sign_in_dec)
    TextView tvSignInDec;
    @BindView(R.id.iv_has_connect)
    ImageView mConnectWifiImage;
    @BindView(R.id.tv_has_connect_name)
    TextView mConnectWifiName;
    @BindView(R.id.tv_has_connect_time)
    TextView mConnectWifiTime;
    @BindView(R.id.pb_scan_loading)
    ProgressView mScanProgressBar;

    private MainWifiListAdapter mAdapter;
    private boolean mIsSign; // 是否已签到
    private BaseTimer mScanWIFITimer;
    private IntentFilter mIntentFilter;
    private WIFIStateChangeReceiver mWIFIStateChangeReceiver;
    private LocationHelper mLocationHelper;
    private double mXCoordinate = 0; // 纬度
    private double mYCoordinate = 0; // 经度
    private AccountInfoRealm mAccountInfoRealm;
    private List<ShareWifiHttpData> mShareWifiHttpDataList;
    private WIFIConnectManager mWIFIConnectManager;
    private WIFIConnectManager.WifiCipherType mWifiCipherType;
    private ShareWifiHttpData mNeedConnectWifi;
    private CurrentWifiInfoRealm mCurrentWifiInfoRealm;

    @OnClick({ R.id.iv_main_personal_enter, R.id.iv_main_share, R.id.tv_sign_in, R.id.layout_main_connect_wifi })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_main_personal_enter:
            startActivity(new Intent(mActivity, PersonalCenterActivity.class));
            break;
        case R.id.iv_main_share:
            String currentWifiSSID = WIFIHelper.getCurrentConnectWIFISSID(mActivity);
            if (!TextUtils.isEmpty(currentWifiSSID)) {
                Intent intent = new Intent(mActivity, WifiShareActivity.class);
                intent.putExtra("title", currentWifiSSID);
                startActivity(intent);
            }
            break;
        case R.id.tv_sign_in:
            if (null == mAccountInfoRealm || TextUtils.isEmpty(mAccountInfoRealm.getMobile()) || TextUtils.isEmpty(mAccountInfoRealm.getId())) {
                // 未登陆帐号，跳转到登陆界面
                startActivity(new Intent(mActivity, LoginActivity.class));
                return;
            }
            if (!mIsSign && null != mPresenter) {
                mPresenter.requestUserSign(mAccountInfoRealm.getMobile());
            }
            break;
        case R.id.layout_main_connect_wifi:
            Intent intent = new Intent(mActivity, WifiDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(WIFIDefine.ACTIVITY_JUMP_FROM, WIFIDefine.JUMP_ACTIVITY.WIFI_MAIN);
            intent.putExtras(bundle);
            startActivityForResult(intent, WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_WIFIDETAIL_PAGE_REQUESTCODE);
            break;
        default:
            break;
        }
    }

    private MainActivity mActivity;
    private MainContract.Presenter mPresenter;

    public static MainFragment getInstance() {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main;
    }

    private void initRecyclerView(List<WIFIInfo> wifiInfoList) {
        if (null == mAdapter) {
            mAdapter = new MainWifiListAdapter(mActivity, wifiInfoList, false);
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<WIFIInfo>() {
                @SuppressLint("HandlerLeak")
                @Override
                public void onItemClick(BaseHolder viewHolder, final WIFIInfo info, int position) {
                    if (null != info) {
                        mNeedConnectWifi = null;
                        mWifiCipherType = null;
                        mWIFIConnectManager = new WIFIConnectManager(mActivity);
                        mWIFIConnectManager.mHandler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                // 操作界面
                                if (null != msg) {
                                    LogHelper.releaseLog(TAG + " Wifi Item Click msg:" + msg.obj);
                                }
                            }
                        };
                        String capabilities = info.getCapabilities().trim();
                        LogHelper.releaseLog(TAG + " Wifi Item Click capabilities:" + capabilities);
                        mWifiCipherType = WIFIConnectManager.WifiCipherType.WIFICIPHER_NOPASS;
                        if (!TextUtils.isEmpty(capabilities)) {
                            if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                                mWifiCipherType = WIFIConnectManager.WifiCipherType.WIFICIPHER_WPA;
                            } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                                mWifiCipherType = WIFIConnectManager.WifiCipherType.WIFICIPHER_WEP;
                            }
                        }
                        if (info.isShared()) {
                            String payInfo = String.format(getResources().getString(R.string.wifi_pay_integral), info.getEarnings());
                            DialogUtils.showDialog(mActivity, getResources().getString(R.string.wifi_need_pay), payInfo,
                                    new DialogUtils.OnDialogClickListener() {
                                        @Override
                                        public void onClick(String content) {
                                            // 取消
                                        }
                                    }, new DialogUtils.OnDialogClickListener() {
                                        @Override
                                        public void onClick(String content) {
                                            // 确定
                                            if (null != mShareWifiHttpDataList && mShareWifiHttpDataList.size() > 0) {
                                                for (int i = 0; i < mShareWifiHttpDataList.size(); i++) {
                                                    ShareWifiHttpData shareWifiHttpData = mShareWifiHttpDataList.get(i);
                                                    if (TextUtils.equals(shareWifiHttpData.getId(), info.getShareWifiId())) {
                                                        mNeedConnectWifi = shareWifiHttpData;
                                                        if (null == mAccountInfoRealm || TextUtils.isEmpty(mAccountInfoRealm.getMobile())
                                                                || TextUtils.isEmpty(mAccountInfoRealm.getId())) {
                                                            return;
                                                        }
                                                        if (null != mPresenter && null != mNeedConnectWifi) {
                                                            mPresenter.requestJoinWifi(mAccountInfoRealm.getMobile(), mNeedConnectWifi.getId(), "1");
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                        } else {
                            if (WIFIHelper.isFreeWifi(info.getCapabilities())) {
                                // 免费wifi直接连接
                                WIFIHelper.disconnectWIFI(mActivity);
                                mWIFIConnectManager.connectWIFI(info.getName(), "", mWifiCipherType);
                            } else {
                                // 付费wifi需要密码连接
                                DialogUtils.showDialog(mActivity, getResources().getString(R.string.wifi_need_pass), "", true,
                                        new DialogUtils.OnDialogClickListener() {
                                            @Override
                                            public void onClick(String content) {
                                                // 取消
                                            }
                                        }, new DialogUtils.OnDialogClickListener() {
                                            @Override
                                            public void onClick(String content) {
                                                // 确定
                                                WIFIHelper.disconnectWIFI(mActivity);
                                                mWIFIConnectManager.connectWIFI(info.getName(), content, mWifiCipherType);
                                            }
                                        });
                            }
                        }
                    }
                }
            });
            recyclerviewMain.setNestedScrollingEnabled(false);
            // 设置布局管理器
            recyclerviewMain.setLayoutManager(new FullyLinearLayoutManager(mActivity));
            recyclerviewMain.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(wifiInfoList);
            mAdapter.notifyDataSetChanged();
        }
        new BaseTimer().startTimer(HIDE_LOADING_TIME, new BaseTimer.TimerCallBack() {
            @Override
            public void callback() {
                mScanProgressBar.setVisibility(View.GONE);
                mScanProgressBar.stopRotateAnimation();
            }
        });
    }

    @Override
    protected void initView() {
        mAccountInfoRealm = AccountHelper.getInstance().getAccountInfo();
        mConnectWifiLayout.setVisibility(View.GONE);
        layoutTips.setFocusable(true);
        layoutTips.setFocusableInTouchMode(true);
        layoutTips.requestFocus();
        layoutTips.requestFocusFromTouch();
        mScanProgressBar.setImageResource(R.drawable.ic_list_loading);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.releaseLog(TAG + "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
        if (resultCode == WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_WIFIDETAIL_PAGE_REQUESTCODE && null != mConnectWifiLayout) {
            mConnectWifiLayout.setVisibility(View.GONE);
        }
    }

    private void initHasConnectWIFIInfo() {
        LogHelper.releaseLog(TAG + "initHasConnectWIFIInfo");
        if (null == mConnectWifiLayout) {
            return;
        }
        WifiInfo wifiInfo = WIFIHelper.getCurrentConnectingWIFI(mActivity);
        String currentWifiName = WIFIHelper.getCurrentConnectWIFISSID(mActivity);
        if (!TextUtils.isEmpty(currentWifiName)) {
            mConnectWifiLayout.setVisibility(View.VISIBLE);
            CurrentWifiInfoRealm currentWifiInfoRealm = CommonUtil.getCurrentConnectWifiRealm();
            String currentWifiMac = WIFIHelper.getCurrentConnectWIFIMac(mActivity);

            // 数据库中当前连接的wifi数据不为空，需要判断当前的连接的wifi和数据库中的wifi信息是否一致
            if (null != currentWifiInfoRealm && TextUtils.equals(currentWifiMac, currentWifiInfoRealm.getMac())
                    && TextUtils.equals(currentWifiName, currentWifiInfoRealm.getName()) && currentWifiInfoRealm.isShared()) {
                // 连接的网络跟数据库中的网络一致，并且是共享类型
                long connectTime = currentWifiInfoRealm.getConnectTime();
                long currentTime = System.currentTimeMillis();
                long alreadyConnectTime = currentTime - connectTime;
                int min = (int) (alreadyConnectTime / (60 * 1000));
                int sec = (int) (alreadyConnectTime % (60 * 1000));
                if (sec > 0) {
                    min++;
                }
                if (min >= 60) {
                    WIFIHelper.disconnectWIFI(mActivity);
                    mShareView.setImageResource(R.drawable.ic_nav_share_pressed);
                    CommonUtil.deleteCurrentWifiRealm();
                    mConnectWifiLayout.setVisibility(View.GONE);
                } else {
                    mConnectWifiTime.setText(String.format(getResources().getString(R.string.wifi_has_connect_time), min + ""));
                    mConnectWifiTime.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mConnectWifiName.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mConnectWifiName.setLayoutParams(params);
                }
            } else {
                mCurrentWifiInfoRealm = new CurrentWifiInfoRealm();
                mCurrentWifiInfoRealm.setShared(false);
                mCurrentWifiInfoRealm.setName(currentWifiName);
                mCurrentWifiInfoRealm.setMac(currentWifiMac);
                mCurrentWifiInfoRealm.setIp(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
                DhcpInfo dhcpInfo = WIFIHelper.getDhcpInfo(mActivity);
                if (null != dhcpInfo) {
                    mCurrentWifiInfoRealm.setGateway(Formatter.formatIpAddress(dhcpInfo.gateway));
                }
                mCurrentWifiInfoRealm.setSignalStrength(wifiInfo.getRssi());
                CommonUtil.saveCurrentConnectWifiRealm(mCurrentWifiInfoRealm);
                mConnectWifiTime.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mConnectWifiName.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                mConnectWifiName.setLayoutParams(params);
            }

            mShareView.setImageResource(R.drawable.share_bg);
            mConnectWifiLayout.setVisibility(View.VISIBLE);
            mConnectWifiName.setText(currentWifiName);
            boolean isLocked = WIFIHelper.checkWifiHasPassword(mActivity, currentWifiName);
            if (isLocked) {
                mConnectWifiImage.setImageResource(R.drawable.ic_common_wifi_locked);
            } else {
                mConnectWifiImage.setImageResource(WIFIHelper.getWIFIStateImageBySignalStrength(WIFIHelper.getCurrentConnectWIFISignalStrength(mActivity)));
            }
        } else {
            mShareView.setImageResource(R.drawable.ic_nav_share_pressed);
            CommonUtil.deleteCurrentWifiRealm();
            mConnectWifiLayout.setVisibility(View.GONE);
        }
    }

    private void startScanWIFIList() {
        LogHelper.releaseLog(TAG + "startScanWIFIList");
        List<ScanResult> wifiList = WIFIHelper.getAllWIFIList(mActivity);
        if (null != wifiList && wifiList.size() > 0) {
            List<WIFIInfo> wifiInfoList = new ArrayList<WIFIInfo>();
            for (int i = 0; i < wifiList.size(); i++) {
                ScanResult scanResult = wifiList.get(i);
                if (null != scanResult) {
                    WifiInfo currentConnectingWIFI = WIFIHelper.getCurrentConnectingWIFI(mActivity);
                    if (null != currentConnectingWIFI && (TextUtils.equals(currentConnectingWIFI.getBSSID(), scanResult.BSSID)
                            || TextUtils.equals(WIFIHelper.getCurrentConnectWIFISSID(mActivity), scanResult.SSID))) {
                        continue;
                    }
                    if (judgeWIFIIsExit(wifiInfoList, scanResult.SSID)) {
                        continue;
                    }
                    WIFIInfo wifiInfo = new WIFIInfo(scanResult.SSID, scanResult.BSSID, scanResult.capabilities, scanResult.level);
                    ShareWifiHttpData shareWifiHttpData = getShareWifiData(scanResult);
                    if (null != shareWifiHttpData && null != wifiInfo) {
                        wifiInfo.setShareWifiId(shareWifiHttpData.getId());
                        wifiInfo.setIsShared(true);
                        wifiInfo.setEarnings(shareWifiHttpData.getEarnings());
                    }
                    wifiInfoList.add(wifiInfo);
                }
            }
            if (wifiInfoList.size() > 0) {
                initRecyclerView(wifiInfoList);
            }
        }
    }

    /**
     * 根据扫描到的wifi信息获取共享wifi信息
     * 
     * @param scanResult
     * @return
     */
    private ShareWifiHttpData getShareWifiData(ScanResult scanResult) {
        if (null != mShareWifiHttpDataList && mShareWifiHttpDataList.size() > 0 && null != scanResult) {
            LogHelper.releaseLog(TAG + "getShareWifiData shareWifiList size:" + mShareWifiHttpDataList.size());
            for (int i = 0; i < mShareWifiHttpDataList.size(); i++) {
                ShareWifiHttpData shareWifiHttpData = mShareWifiHttpDataList.get(i);
                if (TextUtils.equals(shareWifiHttpData.getName(), scanResult.SSID) && TextUtils.equals(shareWifiHttpData.getMac(), scanResult.BSSID)) {
                    LogHelper.releaseLog(TAG + "getShareWifiData shareWifiHttpData:" + shareWifiHttpData.toString() + " scanResult:" + scanResult.toString());
                    return shareWifiHttpData;
                }
            }
        }
        return null;
    }

    /**
     * 判断扫描到的wifi是否存在
     * 
     * @param wifiInfoList
     * @param ssid
     * @return
     */
    private boolean judgeWIFIIsExit(List<WIFIInfo> wifiInfoList, String ssid) {
        if (null != wifiInfoList && wifiInfoList.size() > 0 && !TextUtils.isEmpty(ssid)) {
            for (int i = 0; i < wifiInfoList.size(); i++) {
                if (TextUtils.equals(ssid, wifiInfoList.get(i).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void initData() {
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CurrentWifiInfoRealm currentWifiInfoRealm = CommonUtil.getCurrentConnectWifiRealm();
        if (null != currentWifiInfoRealm && currentWifiInfoRealm.isShared()) {
            WIFIHelper.disconnectWIFI(mActivity);
        }
        if (null != mScanWIFITimer) {
            mScanWIFITimer.killTimer();
            mScanWIFITimer = null;
        }
        if (null != mLocationHelper) {
            mLocationHelper.setLocationCallBack(null);
            mLocationHelper.release();
            mLocationHelper = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.releaseLog(TAG + "onResume!");
        List<String> permissionsList = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.releaseLog(TAG + "onResume no location permission!");
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    WIFIDefine.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            LogHelper.releaseLog(TAG + "onResume has location permission!");
            startLocation();
        }
        mIsSign = CommonUtil.userIsSign();
        doSign();
        if (WifiManager.WIFI_STATE_ENABLED != WIFIHelper.getWIFISwitchState(mActivity)) {
            DialogUtils.showDialog(mActivity, getResources().getString(R.string.wifi_switch_close), getResources().getString(R.string.wifi_switch_need_open),
                    new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onClick(String content) {
                            // 取消
                            mScanProgressBar.setVisibility(View.GONE);
                            registerWIFIStateBroadcast();
                        }
                    }, new DialogUtils.OnDialogClickListener() {
                        @Override
                        public void onClick(String content) {
                            // 确定
                            WIFIHelper.setWIFIEnabled(mActivity, true);
                        }
                    });
        }
    }

    private void startScanWIFITimer() {
        if (null == mScanWIFITimer) {
            mScanWIFITimer = new BaseTimer();
        }
        LogHelper.releaseLog(TAG + "startScanWIFITimer!");
        mScanWIFITimer.startIntervalInstance(SCAN_WIFI_TIME, new BaseTimer.TimerCallBack() {
            @Override
            public void callback() {
                LogHelper.releaseLog(TAG + "startScanWIFITimer callback!");
                mScanProgressBar.setVisibility(View.VISIBLE);
                mScanProgressBar.startRotateAnimation();
                initHasConnectWIFIInfo();
                startScanWIFIList();
            }
        });
    }

    private void registerWIFIStateBroadcast() {
        LogHelper.releaseLog(TAG + "registerWIFIStateBroadcast!");
        try {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            mWIFIStateChangeReceiver = new WIFIStateChangeReceiver();
            mActivity.registerReceiver(mWIFIStateChangeReceiver, mIntentFilter);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "registerWIFIStateBroadcast Error!");
        }
    }

    private void releaseWIFIStateBroadcast() {
        LogHelper.releaseLog(TAG + "releaseWIFIStateBroadcast!");
        try {
            if (null != mWIFIStateChangeReceiver) {
                mActivity.unregisterReceiver(mWIFIStateChangeReceiver);
                mWIFIStateChangeReceiver = null;
            }
            mIntentFilter = null;
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "releaseWIFIStateBroadcast Error!");
        }
    }

    private void doSign() {
        LogHelper.releaseLog(TAG + "doSign mIsSign:" + mIsSign);
        if (mIsSign) {
            tvSignIn.setBackgroundResource(R.drawable.bg_has_sign_in);
            tvSignIn.setTextColor(getResources().getColor(R.color.black_30));
            tvSignIn.setText(R.string.signed);
            ToastUtils.showShort(R.string.sign_success);
        } else {
            tvSignIn.setBackgroundResource(R.drawable.bg_sign_in);
            tvSignIn.setTextColor(getResources().getColor(R.color.color_login_bg));
            tvSignIn.setText(R.string.sign);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mScanWIFITimer) {
            mScanWIFITimer.killTimer();
            mScanWIFITimer = null;
        }
        releaseWIFIStateBroadcast();
        if (null != mScanProgressBar) {
            mScanProgressBar.stopRotateAnimation();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setSignHttpResult(BaseHttpResult<BaseHttpData> signHttpResult) {
        if (null != signHttpResult && WIFIDefine.HttpResultState.SUCCESS.equals(signHttpResult.getStatus())) {
            mIsSign = true;
            doSign();
            AccountHelper.getInstance().addUserSignInfo();
            AccountHelper.getInstance().updataAccountInfo("", "");
        } else {
            ToastUtils.showShort(R.string.sign_fail);
        }
    }

    @Override
    public void setShareWifiListHttpResult(BaseHttpResult<List<ShareWifiHttpData>> wifiListHttpResult) {
        LogHelper.releaseLog(TAG + "setShareWifiListHttpResult result:" + (null != wifiListHttpResult ? wifiListHttpResult.toString() : "is null!"));
        if (null != wifiListHttpResult && WIFIDefine.HttpResultState.SUCCESS.equals(wifiListHttpResult.getStatus())
                && null != wifiListHttpResult.getHttpData()) {
            // 请求共享wifi列表成功
            mShareWifiHttpDataList = wifiListHttpResult.getHttpData();
        }
        startScanWIFITimer();
    }

    @Override
    public void setJoinWifiHttpResult(BaseHttpResult<BaseHttpData> joinWifiHttpResult) {
        LogHelper.releaseLog(TAG + "setJoinWifiHttpResult result:" + (null != joinWifiHttpResult ? joinWifiHttpResult.toString() : "is null!"));
        if (null != joinWifiHttpResult && WIFIDefine.HttpResultState.SUCCESS.equals(joinWifiHttpResult.getStatus())) {
            // 加入wifi消费成功
            if (null != mWIFIConnectManager && null != mNeedConnectWifi && null != mWifiCipherType) {
                WIFIHelper.disconnectWIFI(mActivity);
                mWIFIConnectManager.connectWIFI(mNeedConnectWifi.getName(), mNeedConnectWifi.getPass(), mWifiCipherType);
                mCurrentWifiInfoRealm = new CurrentWifiInfoRealm();
                mCurrentWifiInfoRealm.setShared(true);
                mCurrentWifiInfoRealm.setId(mNeedConnectWifi.getId());
                mCurrentWifiInfoRealm.setName(mNeedConnectWifi.getName());
                mCurrentWifiInfoRealm.setMac(mNeedConnectWifi.getMac());
                mCurrentWifiInfoRealm.setPass(mNeedConnectWifi.getPass());
                mCurrentWifiInfoRealm.setIp(mNeedConnectWifi.getIp());
                mCurrentWifiInfoRealm.setGateway(mNeedConnectWifi.getGateway());
                mCurrentWifiInfoRealm.setXCoordinate(mNeedConnectWifi.getXCoordinate());
                mCurrentWifiInfoRealm.setYCoordinate(mNeedConnectWifi.getYCoordinate());
                mCurrentWifiInfoRealm.setMobile(mNeedConnectWifi.getMobile());
                mCurrentWifiInfoRealm.setAddTime(mNeedConnectWifi.getAddtime());
                mCurrentWifiInfoRealm.setConnectTime(System.currentTimeMillis());
                mCurrentWifiInfoRealm.setEarnings(mNeedConnectWifi.getEarnings());
                CommonUtil.saveCurrentConnectWifiRealm(mCurrentWifiInfoRealm);
                ToastUtils.showShort(R.string.do_not_exit);
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
                WIFIShareInfo wifiShareInfo = new WIFIShareInfo();
                // 经纬度
                try {
                    wifiShareInfo.setXCoordinate(String.valueOf(mXCoordinate));
                    wifiShareInfo.setYCoordinate(String.valueOf(mYCoordinate));
                } catch (Exception e) {
                    LogHelper.errorLog(TAG + "setLocation Exception! msg:" + e.getMessage());
                }
                LogHelper.releaseLog(TAG + "setLocation wifiShareInfo:" + wifiShareInfo.toString());
                if (null == mAccountInfoRealm || TextUtils.isEmpty(mAccountInfoRealm.getMobile()) || TextUtils.isEmpty(mAccountInfoRealm.getId())) {
                    // 未登陆帐号，直接扫描wifi
                    startScanWIFITimer();
                } else {
                    wifiShareInfo.setMobile(mAccountInfoRealm.getMobile());
                    if (null != mPresenter) {
                        mPresenter.requestShareWifiList(wifiShareInfo);
                    }
                }
            } else {
                LogHelper.releaseLog(TAG + "setLocation location is null! startScanThread");
                startScanWIFITimer();
            }
        }
    };

    class WIFIStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                LogHelper.releaseLog(TAG + "WIFIStateChangeReceiver action:" + action + " wifiState:" + wifiState);
                if (TextUtils.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                        // WIFI开关处于打开状态
                        startLocation();
                    } else {
                        // WIFI开关处于关闭状态
                    }
                }
            }
        }
    }
}
