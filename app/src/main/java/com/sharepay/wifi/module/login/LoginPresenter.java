package com.sharepay.wifi.module.login;

import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.LoginRequestService;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;
import com.sharepay.wifi.model.http.TokenHttpData;
import com.sharepay.wifi.util.CommonUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    public void getTokenResult() {
        if (!CommonUtil.tokenIsExpired()) {
            // token未过期
            return;
        }
        mLoginRequestService.getTokenResult(CommonUtil.getDeivceID(), WIFIDefine.APPID, WIFIDefine.APPSECRET).subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .subscribe(new Observer<BaseHttpResult<TokenHttpData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogHelper.releaseLog(TAG + "getTokenResult onSubscribe! Disposable:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(BaseHttpResult<TokenHttpData> tokenHttpData) {
                        if (null != mView && null != tokenHttpData) {
                            LogHelper.releaseLog(TAG + "getTokenResult onNext! tokenData:" + tokenHttpData.toString());
                            TokenHttpData tokenData = tokenHttpData.getHttpData();
                            if (null != tokenData) {
                                CommonUtil.saveToken(tokenData);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "getTokenResult onError! msg:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogHelper.releaseLog(TAG + "getTokenResult onComplete!");
                    }
                });
    }

    @Override
    public void getVerificationCode(String mobile) {
        mLoginRequestService.getVerificationCode(CommonUtil.getDeivceID(), mobile, CommonUtil.getToken()).subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .subscribe(new Observer<BaseHttpResult<BaseHttpData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogHelper.releaseLog(TAG + "getVerificationCode onSubscribe! Disposable:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(BaseHttpResult<BaseHttpData> smsHttpData) {
                        if (null != smsHttpData) {
                            LogHelper.releaseLog(TAG + "getVerificationCode onNext! smsData:" + smsHttpData.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "getVerificationCode onError! msg:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogHelper.releaseLog(TAG + "getVerificationCode onComplete!");
                    }
                });
    }

    @Override
    public void login(String mobile, String code) {
        mLoginRequestService.login(mobile, CommonUtil.getToken(), CommonUtil.getDeivceID(), code).subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .subscribe(new Observer<BaseHttpResult<LoginAccountHttpData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogHelper.releaseLog(TAG + "login onSubscribe! Disposable:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(BaseHttpResult<LoginAccountHttpData> loginAccountHttpData) {
                        if (null != mView && null != loginAccountHttpData) {
                            LogHelper.releaseLog(TAG + "login onNext! loginAccountHttpData:" + loginAccountHttpData.toString());
                            mView.setLoginAccountHttpResult(loginAccountHttpData);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "login onError! msg:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogHelper.releaseLog(TAG + "login onComplete!");
                    }
                });
    }
}