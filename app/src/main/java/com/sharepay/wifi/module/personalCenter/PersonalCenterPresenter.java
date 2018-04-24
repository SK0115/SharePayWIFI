package com.sharepay.wifi.module.personalCenter;

import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.http.PersonalCenterRequestService;
import com.sharepay.wifi.model.http.AppVersionHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

public class PersonalCenterPresenter implements PersonalCenterContract.Presenter {

    private static final String TAG = "PersonalCenterPresenter ";
    private PersonalCenterContract.View mView;
    private PersonalCenterRequestService mPersonalCenterRequestService;

    public PersonalCenterPresenter(PersonalCenterContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mPersonalCenterRequestService = HttpRequestHelper.getInstance().create(PersonalCenterRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void requestAppVersion() {
        HttpRequestHelper.getInstance().requestAppVersion(new BaseHttpObserver<BaseHttpResult<AppVersionHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object appVersionHttpResult) {
                if (null != mView && appVersionHttpResult instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestAppVersion onNext! appVersionHttpResult:" + appVersionHttpResult.toString());
                    mView.setAppVersionHttpResult((BaseHttpResult<AppVersionHttpData>) appVersionHttpResult);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestAppVersion onError! msg:" + e.getMessage());
            }
        }), mPersonalCenterRequestService);
    }
}
