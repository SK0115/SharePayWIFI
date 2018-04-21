package com.sharepay.wifi.module.login;

import com.sharepay.wifi.base.BasePresenter;
import com.sharepay.wifi.base.BaseView;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;

public interface LoginContract {

    interface View extends BaseView<Presenter> {
        /**
         * 设置登陆帐号信息
         * 
         * @param loginHttpData
         */
        void setLoginAccountHttpResult(BaseHttpResult<LoginAccountHttpData> loginHttpData);
    }

    interface Presenter extends BasePresenter {

        /**
         * 获取手机验证码
         * 
         * @param mobile
         *            手机号
         */
        void getVerificationCode(String mobile);

        /**
         * 登陆
         * 
         * @param mobile
         *            手机号
         * @param code
         *            验证码
         */
        void login(String mobile, String code);
    }
}
