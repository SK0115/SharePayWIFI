package com.sharepay.wifi.module.costHistory;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;

import java.util.List;

public interface CostHistoryContract {

    interface View extends BaseView<Presenter> {

        /**
         * 设置用户积分历史
         * 
         * @param userIntegralHistoryHttpDataList
         * @param pageNo
         */
        void setUserIntegralHistoryHttpResult(List<UserIntegralHistoryHttpData> userIntegralHistoryHttpDataList, int pageNo);
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取用户积分历史
         * 
         * @param mobile
         * @param pageNo
         */
        void requestUserIntegralHistory(String mobile, int pageNo);
    }
}
