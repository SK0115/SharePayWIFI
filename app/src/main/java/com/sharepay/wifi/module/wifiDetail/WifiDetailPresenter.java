package com.sharepay.wifi.module.wifiDetail;

import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.NetManager;
import com.sharepay.wifi.http.NetSpeedTestService;
import com.sharepay.wifi.model.http.NetSpeedTestHttpData;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WifiDetailPresenter implements WifiDetailContract.Presenter {
    private static final String TAG = "WifiDetailPresenter ";
    private WifiDetailContract.View mView;
    private NetSpeedTestService mService;

    public WifiDetailPresenter(WifiDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(NetSpeedTestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void getNetSpeedTestResult() {
        mService.getNetSpeedTestResult().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<NetSpeedTestHttpData>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogHelper.releaseLog(TAG + "getNetSpeedTestResult onSubscribe! Disposable:" + d.isDisposed());
            }

            @Override
            public void onNext(NetSpeedTestHttpData netSpeedTestHttpData) {
                LogHelper.releaseLog(TAG + "getNetSpeedTestResult onNext! NetSpeedTestHttpData speedTestUrl:" + netSpeedTestHttpData.getSpeedTestUrl());
                if (null != mView) {
                    mView.setNetSpeedTestUrl(netSpeedTestHttpData.getSpeedTestUrl());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "getNetSpeedTestResult onError! msg:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                LogHelper.releaseLog(TAG + "getNetSpeedTestResult onComplete!");
            }
        });
    }
}
