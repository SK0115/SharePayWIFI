package com.sharepay.wifi.module.wifiShare;

import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.http.WifiShareRequestService;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.info.WIFIShareInfo;

public class WifiSharePresenter implements WifiShareContract.Presenter {

    private static final String TAG = "WifiSharePresenter ";
    private WifiShareContract.View mView;
    private WifiShareRequestService mWifiShareRequestService;

    public WifiSharePresenter(WifiShareContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mWifiShareRequestService = HttpRequestHelper.getInstance().create(WifiShareRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void requestUserShareWifi(WIFIShareInfo wifiShareInfo) {
        HttpRequestHelper.getInstance().requestUserShareWifi(new BaseHttpObserver<BaseHttpResult<BaseHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object wifiShareData) {
                LogHelper.releaseLog(TAG + "requestUserShareWifi onNext! wifiShareData:" + wifiShareData.toString());
                if (null != mView && wifiShareData instanceof BaseHttpResult) {
                    mView.setWifiShareResult((BaseHttpResult<BaseHttpData>) wifiShareData);
                }
            }

            @Override
            public void onError(Throwable e) {
            }
        }), mWifiShareRequestService, wifiShareInfo);
    }
}
