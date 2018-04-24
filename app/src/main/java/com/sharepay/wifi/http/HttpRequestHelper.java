package com.sharepay.wifi.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sharepay.wifi.base.BaseHttpResultFunction;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;
import com.sharepay.wifi.model.http.NetSpeedTestHttpData;
import com.sharepay.wifi.model.http.ShareWifiListHttpData;
import com.sharepay.wifi.model.http.TokenHttpData;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;
import com.sharepay.wifi.model.info.WIFIShareInfo;
import com.sharepay.wifi.util.CommonUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequestHelper {

    private static final String TAG = "HttpRequestHelper ";
    private static final int HTTP_REQUEST_DEFAULT_TIMEOUT = 120;
    private static HttpRequestHelper mHttpRequestHelper;

    private HttpRequestHelper() {
    }

    public static HttpRequestHelper getInstance() {
        if (CommonUtil.checkIsNull(mHttpRequestHelper)) {
            mHttpRequestHelper = new HttpRequestHelper();
        }
        return mHttpRequestHelper;
    }

    public <T> T create(Class<T> service) {
        return new Retrofit.Builder().baseUrl(getBaseUrl(service)).client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(service);
    }

    private <T> String getBaseUrl(Class<T> service) {
        String requestUrl = null;
        try {
            Field field = service.getField("DOMAIN_URL");
            requestUrl = (String) field.get(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    private OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS).readTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 配置request header 添加的token拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                Response response = chain.proceed(newBuilder.build());
                if (null != response) {
                    LogHelper.releaseLog(TAG + "getOkHttpClient response:" + response.toString());
                    LogHelper.releaseLog(TAG + "getOkHttpClient responseBody:" + response.body().string().toString());
                }
                return chain.proceed(newBuilder.build());
            }
        });
        return builder.build();
    }

    /**
     * 生成Observable
     * 
     * @param observable
     * @param observer
     * @param <T>
     */
    private <T> void toObservable(Observable<T> observable, Observer<T> observer) {
        observable.map(new BaseHttpResultFunction<T>()).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 请求token信息
     * 
     * @param observer
     * @param startRequestService
     */
    public void requestToken(Observer<BaseHttpResult<TokenHttpData>> observer, StartRequestService startRequestService) {
        if (!CommonUtil.tokenIsExpired()) {
            // token未过期
            return;
        }
        if (null != observer && null != startRequestService) {
            Observable observable = startRequestService.requestToken(CommonUtil.getDeivceID(), WIFIDefine.APPID, WIFIDefine.APPSECRET);
            toObservable(observable, observer);
        }
    }

    /**
     * 请求手机验证码
     * 
     * @param observer
     * @param loginRequestService
     * @param mobile
     */
    public void requestVerificationCode(Observer<BaseHttpResult<BaseHttpData>> observer, LoginRequestService loginRequestService, String mobile) {
        if (null != observer && null != loginRequestService) {
            Observable observable = loginRequestService.requestVerificationCode(CommonUtil.getDeivceID(), mobile, CommonUtil.getToken());
            toObservable(observable, observer);
        }
    }

    /**
     * 登陆
     * 
     * @param observer
     * @param loginRequestService
     * @param mobile
     * @param code
     */
    public void requestUserlogin(Observer<BaseHttpResult<LoginAccountHttpData>> observer, LoginRequestService loginRequestService, String mobile, String code) {
        if (null != observer && null != loginRequestService) {
            Observable observable = loginRequestService.requestUserlogin(mobile, CommonUtil.getToken(), CommonUtil.getDeivceID(), code);
            toObservable(observable, observer);
        }
    }

    /**
     * 用户签到
     * 
     * @param observer
     * @param mainRequestService
     * @param mobile
     */
    public void requestUserSign(Observer<BaseHttpResult<BaseHttpData>> observer, MainRequestService mainRequestService, String mobile) {
        if (null != observer && null != mainRequestService) {
            Observable observable = mainRequestService.requestUserSign(mobile, CommonUtil.getToken(), CommonUtil.getDeivceID());
            toObservable(observable, observer);
        }
    }

    /**
     * 请求共享wifi列表
     * 
     * @param observer
     * @param mainRequestService
     * @param wifiShareInfo
     */
    public void requestShareWifiList(Observer<BaseHttpResult<List<ShareWifiListHttpData>>> observer, MainRequestService mainRequestService,
            WIFIShareInfo wifiShareInfo) {
        if (null != observer && null != mainRequestService && null != wifiShareInfo) {
            String mobile = wifiShareInfo.getMobile();
            String xcoordinate = wifiShareInfo.getXCoordinate();
            String ycoordinate = wifiShareInfo.getYCoordinate();
            Observable observable = mainRequestService.requestShareWifiList(CommonUtil.getToken(), mobile, CommonUtil.getDeivceID(), xcoordinate, ycoordinate);
            toObservable(observable, observer);
        }
    }

    /**
     * 加入wifi消费接口请求
     * 
     * @param observer
     * @param mainRequestService
     * @param mobile
     * @param id
     * @param time
     */
    public void requestJoinWifi(Observer<BaseHttpResult<BaseHttpData>> observer, MainRequestService mainRequestService, String mobile, String id, String time) {
        if (null != observer && null != mainRequestService) {
            Observable observable = mainRequestService.requestJoinWifi(CommonUtil.getToken(), mobile, CommonUtil.getDeivceID(), id, time);
            toObservable(observable, observer);
        }
    }

    /**
     * 请求用户积分历史信息
     * 
     * @param observer
     * @param costHistoryRequestService
     * @param mobile
     * @param pageNo
     */
    public void requestUserIntegralHistory(Observer<BaseHttpResult<List<UserIntegralHistoryHttpData>>> observer,
            CostHistoryRequestService costHistoryRequestService, String mobile, int pageNo) {
        if (null != observer && null != costHistoryRequestService) {
            Observable observable = costHistoryRequestService.requestUserIntegralHistory(CommonUtil.getToken(), mobile, CommonUtil.getDeivceID(), pageNo);
            toObservable(observable, observer);
        }
    }

    /**
     * 网络测速地址请求
     * 
     * @param observer
     * @param wifiDetailRequestService
     */
    public void requestNetSpeedTestUrl(Observer<NetSpeedTestHttpData> observer, WifiDetailRequestService wifiDetailRequestService) {
        if (null != observer && null != wifiDetailRequestService) {
            Observable observable = wifiDetailRequestService.requestNetSpeedTestUrl();
            toObservable(observable, observer);
        }
    }

    /**
     * wifi分享请求
     * 
     * @param observer
     * @param wifiShareRequestService
     * @param wifiShareInfo
     */
    public void requestUserShareWifi(Observer<BaseHttpResult<BaseHttpData>> observer, WifiShareRequestService wifiShareRequestService,
            WIFIShareInfo wifiShareInfo) {
        if (null != observer && null != wifiShareRequestService && null != wifiShareInfo) {
            String mobile = wifiShareInfo.getMobile();
            String name = wifiShareInfo.getName();
            String pass = wifiShareInfo.getPass();
            String ip = wifiShareInfo.getIp();
            String gateway = wifiShareInfo.getGateway();
            String xcoordinate = wifiShareInfo.getXCoordinate();
            String ycoordinate = wifiShareInfo.getYCoordinate();
            String earnings = wifiShareInfo.getEarnings();
            Observable observable = wifiShareRequestService.requestUserShareWifi(CommonUtil.getToken(), mobile, CommonUtil.getDeivceID(), name, pass, ip,
                    gateway, xcoordinate, ycoordinate, earnings);
            toObservable(observable, observer);
        }
    }
}
