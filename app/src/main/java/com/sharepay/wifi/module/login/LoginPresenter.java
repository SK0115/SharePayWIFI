package com.sharepay.wifi.module.login;

import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.LoginRequestService;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = "LoginPresenter ";
    private LoginContract.View mView;
    private LoginRequestService mLoginRequestService;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mLoginRequestService = HttpRequestHelper.getInstance().create(LoginRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void requestVerificationCode(String mobile) {
        HttpRequestHelper.getInstance().requestVerificationCode(new BaseHttpObserver<BaseHttpResult<BaseHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object smsHttpData) {
                if (smsHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestVerificationCode onNext! smsHttpData:" + smsHttpData.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestVerificationCode onError! msg:" + e.getMessage());
            }
        }), mLoginRequestService, mobile);
    }

    @Override
    public void requestUserlogin(String mobile, String code) {
        HttpRequestHelper.getInstance().requestUserlogin(new BaseHttpObserver<BaseHttpResult<LoginAccountHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object loginAccountHttpData) {
                if (null != mView && loginAccountHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "login onNext! loginAccountHttpData:" + loginAccountHttpData.toString());
                    mView.setLoginAccountHttpResult((BaseHttpResult<LoginAccountHttpData>) loginAccountHttpData);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "login onError! msg:" + e.getMessage());
            }
        }), mLoginRequestService, mobile, code);
    }
}
