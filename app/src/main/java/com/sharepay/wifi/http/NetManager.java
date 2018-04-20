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

public class NetManager {

    private static final String TAG = "NetManager ";
    private static final int DEFAULT_TIMEOUT = 120;
    private static NetManager mNetManager;

    private NetManager() {
    }

    public static NetManager getInstance() {
        if (CommonUtil.checkIsNull(mNetManager)) {
            mNetManager = new NetManager();
        }
        return mNetManager;
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
        final OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // if (BuildConfig.DEBUG) {
        // HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // builder.addInterceptor(interceptor);
        // }
        // 配置request header 添加的token拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                // newBuilder.addHeader("APPVERSION", appVersion + "@" + Api.DEVICE_TYPE);
                LogHelper.releaseLog(TAG + "getOkHttpClient newBuilder:" + newBuilder.build().url().toString());
                return chain.proceed(newBuilder.build());
            }
        });
        LogHelper.releaseLog(TAG + "getOkHttpClient builder:" + builder.build().toString());
        return builder.build();
    }
}
