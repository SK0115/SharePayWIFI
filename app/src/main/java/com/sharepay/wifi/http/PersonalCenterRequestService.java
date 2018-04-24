package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.AppVersionHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 个人中心页网络请求
 */
public interface PersonalCenterRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/user/app_version")
    Observable<BaseHttpResult<AppVersionHttpData>> requestAppVersion(@Query("token") String token, @Query("userid") String userid);
}
