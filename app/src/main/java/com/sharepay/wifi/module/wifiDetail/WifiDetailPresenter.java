package com.sharepay.wifi.module.wifiDetail;

import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.http.WifiDetailRequestService;
import com.sharepay.wifi.model.http.NetSpeedTestHttpData;

public class WifiDetailPresenter implements WifiDetailContract.Presenter {
    private static final String TAG = "WifiDetailPresenter ";
    private WifiDetailContract.View mView;
    private WifiDetailRequestService mWifiDetailRequestService;

    public WifiDetailPresenter(WifiDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mWifiDetailRequestService = HttpRequestHelper.getInstance().create(WifiDetailRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void requestNetSpeedTestUrl() {
        HttpRequestHelper.getInstance().requestNetSpeedTestUrl(new BaseHttpObserver<NetSpeedTestHttpData>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object netSpeedTestHttpData) {
                LogHelper.releaseLog(TAG + "requestNetSpeedTestUrl onNext! NetSpeedTestHttpData:" + netSpeedTestHttpData.toString());
                if (null != mView && netSpeedTestHttpData instanceof NetSpeedTestHttpData) {
                    mView.setNetSpeedTestUrl(((NetSpeedTestHttpData) netSpeedTestHttpData).getSpeedTestUrl());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestNetSpeedTestUrl onError! msg:" + e.getMessage());
            }
        }), mWifiDetailRequestService);
    }
}
