package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;
import com.sharepay.wifi.model.http.TokenHttpData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 登陆页面相关网络请求
 */
public interface LoginRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/auth")
    Observable<BaseHttpResult<TokenHttpData>> getTokenResult(@Query("userid") String userid, @Query("appid") String appid, @Query("secret") String secret);

    @GET("/wifi_api/user/sms")
    Observable<BaseHttpResult<BaseHttpData>> getVerificationCode(@Query("userid") String userid, @Query("mobile") String mobile, @Query("token") String token);

    @GET("/wifi_api/user/login")
    Observable<BaseHttpResult<LoginAccountHttpData>> login(@Query("mobile") String mobile, @Query("token") String token, @Query("userid") String userid,
            @Query("code") String code);
}
