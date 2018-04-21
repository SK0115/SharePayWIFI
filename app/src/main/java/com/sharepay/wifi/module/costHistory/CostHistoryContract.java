package com.sharepay.wifi.module.costHistory;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;

public interface CostHistoryContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取用户积分历史
         * 
         * @param mobile
         * @param pageNo
         */
        void getUsrIntegralHistory(String mobile, int pageNo);
    }
}
