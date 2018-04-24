package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.ShareWifiListHttpData;

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

    @GET("/wifi_api/user/share_list")
    Observable<BaseHttpResult<ShareWifiListHttpData>> requestShareWifiList(@Query("token") String token, @Query("mobile") String mobile,
            @Query("userid") String userid, @Query("x_coordinate") String xcoordinate, @Query("y_coordinate") String ycoordinate);

    @GET("/wifi_api/user/user_expense")
    Observable<BaseHttpResult<BaseHttpData>> requestJoinWifi(@Query("token") String token, @Query("mobile") String mobile, @Query("userid") String userid,
            @Query("id") String id, @Query("time") String time);
}
