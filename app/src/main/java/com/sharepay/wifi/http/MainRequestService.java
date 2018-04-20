package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.BaseHttpData;
import com.sharepay.wifi.model.BaseHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 主页面相关网络请求
 */
public interface MainRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/user/sign")
    Observable<BaseHttpResult<BaseHttpData>> sign(@Query("mobile") String mobile, @Query("token") String token, @Query("userid") String userid);
}
