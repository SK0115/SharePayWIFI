package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * wifi共享页网络请求
 */
public interface WifiShareRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/user/share")
    Observable<BaseHttpResult<BaseHttpData>> requestUserShareWifi(@Query("token") String token, @Query("mobile") String mobile, @Query("userid") String userid,
            @Query("name") String name, @Query("pass") String pass, @Query("ip") String ip, @Query("gateway") String gateway,
            @Query("x_coordinate") String xcoordinate, @Query("y_coordinate") String ycoordinate, @Query("earnings") String earnings);
}
