package com.sharepay.wifi.module.costHistory;

import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.CostHistoryRequestService;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.util.CommonUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CostHistoryPresenter implements CostHistoryContract.Presenter {

    private final String TAG = "CostHistoryPresenter ";
    private CostHistoryContract.View mView;
    private CostHistoryRequestService mCostHistoryRequestService;

    public CostHistoryPresenter(CostHistoryContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mCostHistoryRequestService = HttpRequestHelper.getInstance().create(CostHistoryRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void getUsrIntegralHistory(String mobile, int pageNo) {
        mCostHistoryRequestService.getUsrIntegralHistory(CommonUtil.getToken(), mobile, CommonUtil.getDeivceID(), pageNo).subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .subscribe(new Observer<BaseHttpResult<BaseHttpData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogHelper.releaseLog(TAG + "getUsrIntegralHistory onSubscribe! Disposable:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(BaseHttpResult<BaseHttpData> smsHttpData) {
                        if (null != smsHttpData) {
                            LogHelper.releaseLog(TAG + "getUsrIntegralHistory onNext! smsData:" + smsHttpData.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "getUsrIntegralHistory onError! msg:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogHelper.releaseLog(TAG + "getUsrIntegralHistory onComplete!");
                    }
                });
    }
}
