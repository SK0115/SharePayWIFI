package com.sharepay.wifi.base;

import com.sharepay.wifi.define.WIFIDefine.HttpRequestCallBack;
import com.sharepay.wifi.helper.LogHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 基础Observer
 * 
 * @param <T>
 */
public class BaseHttpObserver<T> implements Observer<T> {

    private static final String TAG = "BaseHttpObserver ";

    private HttpRequestCallBack mHttpRequestCallBack;

    public BaseHttpObserver(HttpRequestCallBack callBack) {
        mHttpRequestCallBack = callBack;
    }

    @Override
    public void onSubscribe(Disposable d) {
        LogHelper.releaseLog(TAG + "onSubscribe! Disposable:" + d.isDisposed());
    }

    @Override
    public void onNext(T t) {
        LogHelper.releaseLog(TAG + "onNext! httpData:" + t.toString());
        if (null != mHttpRequestCallBack) {
            mHttpRequestCallBack.onNext(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        LogHelper.errorLog(TAG + "onError! msg:" + e.getMessage());
        if (null != mHttpRequestCallBack) {
            mHttpRequestCallBack.onError(e);
        }
    }

    @Override
    public void onComplete() {
        LogHelper.releaseLog(TAG + "onComplete!");
    }
}
