package com.sharepay.wifi.activity.wifidetail;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;

public interface WifiDetailContract {
    interface View extends BaseView<Presenter> {
        void setNetSpeedUrl(String speedUrl);
    }

    interface Presenter extends BasePresenter {
        void getNetResult();
    }
}
