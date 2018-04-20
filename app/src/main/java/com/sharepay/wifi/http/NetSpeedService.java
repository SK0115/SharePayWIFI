package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.SpeedDownloadInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NetSpeedService {

    String DOMAIN_URL = DomainDefine.NET_SPEED_URL;

    @GET("/upgrade/Service/optimizationTools.jsp")
    Observable<SpeedDownloadInfo> getSpeedNetResult();
}
