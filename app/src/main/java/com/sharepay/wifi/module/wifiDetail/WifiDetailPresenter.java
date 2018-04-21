package com.sharepay.wifi.module.wifiDetail;

import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.NetManager;
import com.sharepay.wifi.http.NetSpeedService;
import com.sharepay.wifi.model.SpeedDownloadInfo;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WifiDetailPresenter implements WifiDetailContract.Presenter {
    private static final String TAG = "WifiDetailPresenter ";
    private WifiDetailContract.View mView;
    private NetSpeedService mService;

    public WifiDetailPresenter(WifiDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mService = NetManager.getInstance().create(NetSpeedService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void getNetResult() {
        mService.getSpeedNetResult().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SpeedDownloadInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogHelper.releaseLog(TAG + "onSubscribe");
            }

            @Override
            public void onNext(SpeedDownloadInfo speedDownloadInfo) {
                LogHelper.releaseLog(TAG + "onNext SpeedDownloadInfo speedTestUrl:" + speedDownloadInfo.getSpeedTestUrl());
                if (null != mView) {
                    mView.setNetSpeedUrl(speedDownloadInfo.getSpeedTestUrl());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "getSpeedNetResult is Error!");
            }

            @Override
            public void onComplete() {
                LogHelper.releaseLog(TAG + "getNetResult onComplete");
            }
        });
    }
}
