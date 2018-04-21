package com.sharepay.wifi.module.wifiDetail;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;

public interface WifiDetailContract {
    interface View extends BaseView<Presenter> {
        void setNetSpeedTestUrl(String speedUrl);
    }

    interface Presenter extends BasePresenter {
        void requestNetSpeedTestUrl();
    }
}
