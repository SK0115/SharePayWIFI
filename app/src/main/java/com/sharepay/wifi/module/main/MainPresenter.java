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
import com.sharepay.wifi.model.http.ShareWifiListHttpData;
import com.sharepay.wifi.model.info.WIFIShareInfo;

import java.util.List;

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

    @Override
    public void requestShareWifiList(WIFIShareInfo wifiShareInfo) {
        HttpRequestHelper.getInstance().requestShareWifiList(new BaseHttpObserver<BaseHttpResult<List<ShareWifiListHttpData>>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object shareWifiListHttpData) {
                if (null != mView && shareWifiListHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestShareWifiList onNext! shareWifiListHttpData:" + shareWifiListHttpData.toString());
                    mView.setShareWifiListHttpResult((BaseHttpResult<ShareWifiListHttpData>) shareWifiListHttpData);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestShareWifiList onError! msg:" + e.getMessage());
            }
        }), mMainRequestService, wifiShareInfo);
    }

    @Override
    public void requestJoinWifi(String mobile, String id, String time) {
        HttpRequestHelper.getInstance().requestJoinWifi(new BaseHttpObserver<BaseHttpResult<BaseHttpData>>(new HttpRequestCallBack() {
            @Override
            public void onNext(Object joinWifiHttpData) {
                if (null != mView && joinWifiHttpData instanceof BaseHttpResult) {
                    LogHelper.releaseLog(TAG + "requestJoinWifi onNext! joinWifiHttpData:" + joinWifiHttpData.toString());
                    mView.setJoinWifiHttpResult((BaseHttpResult<BaseHttpData>) joinWifiHttpData);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.errorLog(TAG + "requestJoinWifi onError! msg:" + e.getMessage());
            }
        }), mMainRequestService, mobile, id, time);
    }
}
