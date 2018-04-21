package com.sharepay.wifi.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.util.CommonUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequestHelper {

    private static final String TAG = "HttpRequestHelper ";
    private static final int HTTP_REQUEST_DEFAULT_TIMEOUT = 120;
    private static HttpRequestHelper mHttpRequestHelper;

    private HttpRequestHelper() {
    }

    public static HttpRequestHelper getInstance() {
        if (CommonUtil.checkIsNull(mHttpRequestHelper)) {
            mHttpRequestHelper = new HttpRequestHelper();
        }
        return mHttpRequestHelper;
    }

    public <T> T create(Class<T> service) {
        return new Retrofit.Builder().baseUrl(getBaseUrl(service)).client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build().create(service);
    }

    private <T> String getBaseUrl(Class<T> service) {
        String requestUrl = null;
        try {
            Field field = service.getField("DOMAIN_URL");
            requestUrl = (String) field.get(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestUrl;
    }

    public OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS).readTimeout(HTTP_REQUEST_DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 配置request header 添加的token拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                Response response = chain.proceed(newBuilder.build());
                if (null != response) {
                    LogHelper.releaseLog(TAG + "getOkHttpClient response:" + response.toString());
                    LogHelper.releaseLog(TAG + "getOkHttpClient responseBody:" + response.body().string().toString());
                }
                return chain.proceed(newBuilder.build());
            }
        });
        return builder.build();
    }
}
