package com.sharepay.wifi.module.main;

import android.content.Context;
import android.widget.Toast;

import com.sharepay.wifi.R;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.http.MainRequestService;
import com.sharepay.wifi.http.NetManager;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.util.CommonUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private final String TAG = "MainPresenter ";
    private Context mContext;
    private MainContract.View mView;
    private MainRequestService mRequestService;

    public MainPresenter(Context context, MainContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
        mRequestService = NetManager.getInstance().create(MainRequestService.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void userSign(String mobile) {
        mRequestService.sign(mobile, CommonUtil.getToken(), CommonUtil.getDeivceID()).subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .subscribe(new Observer<BaseHttpResult<BaseHttpData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogHelper.releaseLog(TAG + "userSign onSubscribe! Disposable:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(BaseHttpResult<BaseHttpData> signHttpData) {
                        if (null != mView && null != signHttpData) {
                            LogHelper.releaseLog(TAG + "userSign onNext! signHttpData:" + signHttpData.toString());
                            mView.setSignHttpResult(signHttpData);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHelper.errorLog(TAG + "userSign onError! msg:" + e.getMessage());
                        Toast.makeText(mContext, mContext.getString(R.string.sign_fail), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        LogHelper.releaseLog(TAG + "userSign onComplete!");
                    }
                });
    }
}
