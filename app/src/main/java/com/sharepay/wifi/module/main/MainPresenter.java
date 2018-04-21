package com.sharepay.wifi.module.main;

import android.content.Context;
import android.widget.Toast;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseHttpObserver;
import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.MainRequestService;
import com.sharepay.wifi.http.HttpRequestHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

public class MainPresenter implements MainContract.Presenter {

    private final String TAG = "MainPresenter ";
    private Context mContext;
    private MainContract.View mView;
    private MainRequestService mMainRequestService;

    public MainPresenter(Context context, MainContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
        mMainRequestService = HttpRequestHelper.getInstance().create(MainRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void requestUserSign(String mobile) {
        HttpRequestHelper.getInstance().requestUserSign(new BaseHttpObserver<BaseHttpResult<BaseHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object signHttpData) {
                if (null != mView && signHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestUserSign onNext! signHttpData:" + signHttpData.toString());
                    mView.setSignHttpResult((BaseHttpResult<BaseHttpData>) signHttpData);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestUserSign onError! msg:" + e.getMessage());
                Toast.makeText(mContext, mContext.getString(R.string.sign_fail), Toast.LENGTH_SHORT).show();
            }
        }), mMainRequestService, mobile);
    }
}
