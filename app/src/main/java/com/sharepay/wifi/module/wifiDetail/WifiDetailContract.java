package com.sharepay.wifi.module.wifiDetail;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;

public interface WifiDetailContract {
    interface View extends BaseView<Presenter> {

        /**
         * 设置网络测速地址
         * 
         * @param speedUrl
         */
        void setNetSpeedTestUrl(String speedUrl);
    }

    interface Presenter extends BasePresenter {

        /**
         * 请求网络测速地址
         */
        void requestNetSpeedTestUrl();
    }
}
