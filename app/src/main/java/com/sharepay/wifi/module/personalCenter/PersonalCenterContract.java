package com.sharepay.wifi.module.personalCenter;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.AppVersionHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

public interface PersonalCenterContract {

    interface View extends BaseView<Presenter> {

        /**
         * 设置app升级信息
         * 
         * @param appVersionHttpResult
         */
        void setAppVersionHttpResult(BaseHttpResult<AppVersionHttpData> appVersionHttpResult);
    }

    interface Presenter extends BasePresenter {

        /**
         * 请求app升级信息
         */
        void requestAppVersion();
    }
}
