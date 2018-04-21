package com.sharepay.wifi.module.main;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        /**
         * 设置签到信息
         * 
         * @param signHttpResult
         */
        void setSignHttpResult(BaseHttpResult<BaseHttpData> signHttpResult);
    }

    interface Presenter extends BasePresenter {

        /**
         * 用户签到
         * 
         * @param mobile
         */
        void userSign(String mobile);
    }
}
