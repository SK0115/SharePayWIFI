package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.TokenHttpData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StartRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/auth")
    Observable<BaseHttpResult<TokenHttpData>> requestToken(@Query("userid") String userid, @Query("appid") String appid, @Query("secret") String secret);
}
