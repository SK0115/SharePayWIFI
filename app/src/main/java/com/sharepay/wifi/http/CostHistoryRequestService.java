package com.sharepay.wifi.http;

import com.sharepay.wifi.define.DomainDefine;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.UserIntegralHistoryHttpData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 用户积分历史页面相关网络请求
 */
public interface CostHistoryRequestService {

    String DOMAIN_URL = DomainDefine.DOMAIN_URL;

    @GET("/wifi_api/user/user_history")
    Observable<BaseHttpResult<List<UserIntegralHistoryHttpData>>> requestUserIntegralHistory(@Query("token") String token, @Query("mobile") String mobile,
            @Query("userid") String userid, @Query("pageNo") int pageNo);
}
