package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 主页网络请求
 */
public interface MainRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/user/sign")
    Observable<BaseHttpResult<BaseHttpData>> requestUserSign(@Query("mobile") String mobile, @Query("token") String token, @Query("userid") String userid);
}
