package com.sharepay.wifi.module.wifiDetail;

import android.annotation.SuppressLint;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.wifiDetail.WifiDetailActivity;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.helper.NetSpeedDownload;
import com.sharepay.wifi.helper.WIFIHelper;
import com.sharepay.wifi.model.realm.CurrentWifiInfoRealm;
import com.sharepay.wifi.util.CommonUtil;
import com.sharepay.wifi.util.PreferenceUtil;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.OnClick;

public class WifiDetailFragment extends BaseFragment implements WifiDetailContract.View {

    private static final String TAG = "WifiDetailFragment ";
    private static final int SPEEDNUM = 1;
    private static final int SPEEDFINISH = 2;
    private static final int NOTNET = 3;

    @BindView(R.id.tv_network_signal_strength)
    TextView mNetWorkSignalStrengthText;
    @BindView(R.id.tv_network_ip)
    TextView mNetWorkIPText;
    @BindView(R.id.tv_network_gateway)
    TextView mNetWorkGateWayText;

    private LinearLayout mLayoutSpeed;
    private TextView mSpeedFinishText;
    private TextView mSpeedFinishUnitText;
    private AVLoadingIndicatorView mAviSpeedProgress;
    private ImageView mPointView;
    private TextView mUnitText;
    private TextView mSpeedText;
    private TextView mTvSpeedDec;

    private float lastPosition = 0;
    private float newPosition = 0;
    private boolean flag = false;
    private RotateAnimation fristRotaAinimation;
    private NetSpeedDownload download = null;

    @OnClick({ R.id.wifi_detail_iv_back, R.id.layout_signal_strength, R.id.tv_network_disconnect, R.id.tv_network_speed })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.wifi_detail_iv_back:
            if (null != download) {
                download.stopSpeed();
            }
            mActivity.finish();
            break;
        case R.id.layout_signal_strength:
            break;
        case R.id.tv_network_speed:
            showSpeedDialog();
            break;
        case R.id.tv_network_disconnect:
            CurrentWifiInfoRealm currentWifiInfoRealm = CommonUtil.getCurrentConnectWifiRealm();
            if (null != currentWifiInfoRealm && currentWifiInfoRealm.isShared()) {
                WIFIHelper.disconnectWIFI(mActivity);
                WIFIHelper.removeWifiBySsid(mActivity);
            } else {
                WIFIHelper.disconnectWIFI(mActivity);
            }
            mActivity.finish();
            break;
        default:
            break;
        }
    }

    private WifiDetailContract.Presenter mPresenter;
    private WifiDetailActivity mActivity;

    public static WifiDetailFragment getInstance() {
        WifiDetailFragment mWifiDetailFragment = new WifiDetailFragment();
        Bundle bundle = new Bundle();
        mWifiDetailFragment.setArguments(bundle);
        return mWifiDetailFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_wifi_detail;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mActivity = (WifiDetailActivity) getActivity();
    }

    @Override
    public void setPresenter(WifiDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setNetSpeedTestUrl(String speedUrl) {
        LogHelper.releaseLog(TAG + "setNetSpeedUrl speedUrl:" + speedUrl);
        if (!TextUtils.isEmpty(speedUrl)) {
            PreferenceUtil.getInstance().saveStringValue(WIFIDefine.KEY_PREFERENCE_SPEEDDETECTIONURL, speedUrl);
            if (null != download) {
                download.startSpeed(speedUrl);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WifiInfo wifiInfo = WIFIHelper.getCurrentConnectingWIFI(mActivity);
        if (null != wifiInfo) {
            mNetWorkSignalStrengthText.setText(WIFIHelper.getWIFIStateStringBySignalStrength(wifiInfo.getRssi()));
            mNetWorkIPText.setText(Formatter.formatIpAddress(wifiInfo.getIpAddress()));
            DhcpInfo dhcpInfo = WIFIHelper.getDhcpInfo(mActivity);
            if (null != dhcpInfo) {
                mNetWorkGateWayText.setText(Formatter.formatIpAddress(dhcpInfo.gateway));
            }
        }
    }

    private void showSpeedDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.layout_dialog_wifi_detail);
        window.setBackgroundDrawableResource(R.color.transparent);
        mAviSpeedProgress = window.findViewById(R.id.avi_speed_progress);
        mAviSpeedProgress.setVisibility(View.VISIBLE);
        mLayoutSpeed = window.findViewById(R.id.layout_speed);
        mLayoutSpeed.setVisibility(View.INVISIBLE);
        mSpeedFinishText = window.findViewById(R.id.tv_speed_num);
        mSpeedFinishUnitText = window.findViewById(R.id.tv_spped_unit);
        mTvSpeedDec = window.findViewById(R.id.tv_speed_dec);
        mSpeedText = window.findViewById(R.id.tv_speed_num_down);
        mPointView = window.findViewById(R.id.iv_speed_num_point);
        mUnitText = window.findViewById(R.id.tv_speed_num_unit_down);
        window.findViewById(R.id.iv_colse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (null == download) {
            download = new NetSpeedDownload(mNetSpeedCallBack);
        }
        String downUrl = PreferenceUtil.getInstance().getStringValue(WIFIDefine.KEY_PREFERENCE_SPEEDDETECTIONURL, "");
        LogHelper.releaseLog(TAG + "downUrl = " + downUrl);
        if (TextUtils.isEmpty(downUrl)) {
            mPresenter.requestNetSpeedTestUrl();
        } else {
            download.startSpeed(downUrl);
        }
        setSpeed(0);
    }

    private void setSpeed(float speed) {
        setSpeedText(speed);
        newPosition = getComputAngle(speed);
        long duration = 1000;
        if (speed == 0f) {
            duration = 0;
        }
        if (lastPosition == 0) {
            rotationAinimation(lastPosition, newPosition, duration);
        } else if (flag) {
            setartRotationAinimation(lastPosition, newPosition);
            lastPosition = newPosition;
        }
    }

    private void pointerBack() {
        flag = false;
        lastPosition = 0;
        newPosition = 0;
        fristRotaAinimation = null;
        setSpeedText(0);
        setartRotationAinimation(0, 0);
    }

    private void error() {
        pointerBack();
        mSpeedText.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void setSpeedText(float speed) {
        LogHelper.releaseLog(TAG + "speed = " + speed);
        if (speed < 1000f) {
            String t = String.format("%.0f", speed);
            mSpeedText.setText(t);
            mUnitText.setText("Kb/s");
            mSpeedFinishText.setText(t);
            mSpeedFinishUnitText.setText("Kb/s");
        } else {
            String t = String.format("%.1f", (speed / 1024));
            mSpeedText.setText(t);
            mUnitText.setText("Mb/s");
            mSpeedFinishText.setText(t);
            mSpeedFinishUnitText.setText("Mb/s");
        }
    }

    private float getComputAngle(float speed) {
        float angle = 0;
        if (speed >= 0 && speed <= 256) {
            float single = 30f / 256f;
            angle = -140 + single * speed;
        } else if (speed > 256 && speed <= 512) {
            float single = 40f / 256f;
            angle = -110 + single * (speed - 256f);
        } else if (speed > 512 && speed <= 1024) {
            float single = 40f / 512f;
            angle = -70 + single * (speed - 512);
        } else if (speed > 1024 && speed <= 5 * 1024) {
            float single = 60f / (4 * 1024f);
            angle = -30 + single * (speed - 1024);
        } else if (speed > 5 * 1024 && speed <= 10 * 1024) {
            float single = 40f / (5 * 1024f);
            angle = 30 + single * (speed - 5 * 1024);
        } else if (speed > 10 * 1024 && speed <= 20 * 1024) {
            float single = 40f / (10 * 1024f);
            angle = 70 + single * (speed - 10 * 1024);
        } else if (speed > 20 * 1024 && speed <= 50 * 1024) {
            float single = 30f / (30 * 1024f);
            angle = 110 + single * (speed - 20 * 1024);
        } else if (speed > 50 * 1024) {
            angle = 140;
        }
        return angle;
    }

    private void rotationAinimation(float start, float end, long duration) {
        if (null == fristRotaAinimation) {
            fristRotaAinimation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            fristRotaAinimation.setDuration(duration);
            fristRotaAinimation.setFillAfter(true);
            mPointView.startAnimation(fristRotaAinimation);
            fristRotaAinimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    lastPosition = newPosition;
                    flag = true;
                }
            });
        }
    }

    private void setartRotationAinimation(float start, float end) {
        RotateAnimation rotaAinimation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotaAinimation.setDuration(0);
        rotaAinimation.setFillAfter(true);
        mPointView.startAnimation(rotaAinimation);
    }

    private void downloadFinish() {
        LogHelper.releaseLog(TAG + "downloadFinish:---=");
        if (null != mLayoutSpeed) {
            mLayoutSpeed.setVisibility(View.VISIBLE);
        }
        if (null != mAviSpeedProgress) {
            mAviSpeedProgress.setVisibility(View.INVISIBLE);
        }
        mTvSpeedDec.setVisibility(View.VISIBLE);
        String speedResult = String.format(mActivity.getResources().getString(R.string.network_speed_result), "10");
        mTvSpeedDec.setText(speedResult);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SPEEDNUM:
                if (null != msg.obj) {
                    long speed = (Long) msg.obj;
                    setSpeed((int) speed);
                }
                mTvSpeedDec.setText(R.string.detecting_network);
                mTvSpeedDec.setVisibility(View.VISIBLE);
                break;
            case SPEEDFINISH:
                downloadFinish();
                break;
            case NOTNET:
                mTvSpeedDec.setVisibility(View.VISIBLE);
                mTvSpeedDec.setText(R.string.network_detect_speed_fail_notification);
                error();
                break;

            }
        }
    };

    private NetSpeedDownload.NetSpeedCallBack mNetSpeedCallBack = new NetSpeedDownload.NetSpeedCallBack() {

        @Override
        public void speed(long kb) {
            Message msg = new Message();
            msg.what = SPEEDNUM;
            msg.obj = kb;
            mHandler.sendMessage(msg);
        }

        @Override
        public void noNet() {
            Message msg = new Message();
            msg.what = NOTNET;
            mHandler.sendMessage(msg);

        }

        @Override
        public void speedFinish() {
            Message msg = new Message();
            msg.what = SPEEDFINISH;
            mHandler.sendMessage(msg);
        }
    };
}
