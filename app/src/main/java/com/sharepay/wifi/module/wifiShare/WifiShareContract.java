package com.sharepay.wifi.module.wifiShare;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.info.WIFIShareInfo;

public interface WifiShareContract {

    interface View extends BaseView<Presenter> {

        /**
         * 设置wifi分享结果
         * 
         * @param httpResult
         */
        void setWifiShareResult(BaseHttpResult<BaseHttpData> httpResult);
    }

    interface Presenter extends BasePresenter {

        /**
         * wifi分享请求
         * 
         * @param wifiShareInfo
         */
        void requestUserShareWifi(WIFIShareInfo wifiShareInfo);
    }
}
