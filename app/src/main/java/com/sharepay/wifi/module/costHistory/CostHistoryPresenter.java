package com.sharepay.wifi.module.costHistory;

import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.CostHistoryRequestService;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;

import java.util.List;

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
    public void requestUserIntegralHistory(String mobile, final int pageNo) {
        HttpRequestHelper.getInstance()
                .requestUserIntegralHistory(new BaseHttpObserver<BaseHttpResult<List<UserIntegralHistoryHttpData>>>(new HttpRequestCallBack() {
                    @Override
                    public void onNext(Object userIntegralHistoryHttpData) {
                        if (null != mView && userIntegralHistoryHttpData instanceof BaseHttpResult) {
                            LogHelper.releaseLog(
                                    TAG + "requestUserIntegralHistory onNext! userIntegralHistoryHttpData:" + userIntegralHistoryHttpData.toString());
                            mView.setUserIntegralHistoryHttpResult(
                                    ((BaseHttpResult<List<UserIntegralHistoryHttpData>>) userIntegralHistoryHttpData).getHttpData(), pageNo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "requestUserIntegralHistory onError! msg:" + e.getMessage());
                    }
                }), mCostHistoryRequestService, mobile, pageNo);
    }
}
