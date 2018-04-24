package com.sharepay.wifi.module.main;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.ShareWifiListHttpData;
import com.sharepay.wifi.model.info.WIFIShareInfo;

public interface MainContract {
    interface View extends BaseView<Presenter> {

        /**
         * 设置签到信息
         * 
         * @param signHttpResult
         */
        void setSignHttpResult(BaseHttpResult<BaseHttpData> signHttpResult);

        /**
         * 设置共享wifi列表数据
         * 
         * @param wifiListHttpResult
         */
        void setShareWifiListHttpResult(BaseHttpResult<ShareWifiListHttpData> wifiListHttpResult);

        /**
         * 设置加入wifi结果
         * 
         * @param joinWifiHttpResult
         */
        void setJoinWifiHttpResult(BaseHttpResult<BaseHttpData> joinWifiHttpResult);
    }

    interface Presenter extends BasePresenter {

        /**
         * 用户签到
         * 
         * @param mobile
         */
        void requestUserSign(String mobile);

        /**
         * 请求wifi共享列表
         * 
         * @param wifiShareInfo
         */
        void requestShareWifiList(WIFIShareInfo wifiShareInfo);

        /**
         * 用户加入wifi消费
         * 
         * @param mobile
         * @param id
         * @param time
         */
        void requestJoinWifi(String mobile, String id, String time);
    }
}
