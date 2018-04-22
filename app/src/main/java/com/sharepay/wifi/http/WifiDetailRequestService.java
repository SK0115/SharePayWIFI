package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.NetSpeedTestHttpData;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * wifi详情页网络请求
 */
public interface WifiDetailRequestService {

    String DOMAIN_URL = DomainDefine.NET_SPEED_URL;

    @GET("/upgrade/Service/optimizationTools.jsp")
    Observable<NetSpeedTestHttpData> requestNetSpeedTestUrl();
}
