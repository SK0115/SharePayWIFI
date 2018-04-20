package com.sharepay.wifi.http;

import com.sharepay.wifi.helper.LogHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHelper {

    private final String TAG = "HttpHelper ";

    public HttpHelper() {
    }

    /**
     * get的同步请求
     * 
     * @param url
     *            请求地址
     */
    public void getDatasync(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();// 创建OkHttpClient对象
                    Request request = new Request.Builder().url(url)// 请求接口。如果需要传参拼接到接口后面。
                            .build();// 创建Request 对象
                    Response response = client.newCall(request).execute();// 得到Response 对象
                    if (response.isSuccessful()) {
                        // 此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    LogHelper.errorLog(TAG + "getDatasync Exception! message:" + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * get的异步请求
     * 
     * @param url
     *            请求地址
     */
    public void getDataAsync(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {// 回调的方法执行在子线程。
                }
            }
        });
    }

    /**
     * post的同步请求
     *
     * @param url
     *            请求地址
     */
    public void postDatasync(final String url, final FormBody.Builder formBody) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();// 创建OkHttpClient对象
                    // FormBody.Builder formBody = new FormBody.Builder();// 创建表单请求体
                    // formBody.add("username", "zhangsan");// 传递键值对参数
                    Request request = new Request.Builder()// 创建Request 对象。
                            .url(url).post(formBody.build())// 传递请求体
                            .build();
                    Response response = client.newCall(request).execute();// 得到Response 对象
                    if (response.isSuccessful()) {
                        // 此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    LogHelper.errorLog(TAG + "postDatasync Exception! message:" + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * post的异步请求
     * 
     * @param url
     *            请求地址
     */
    public void postDataAsync(String url, FormBody.Builder formBody) {
        OkHttpClient client = new OkHttpClient();// 创建OkHttpClient对象。
        // FormBody.Builder formBody = new FormBody.Builder();// 创建表单请求体
        // formBody.add("username", "zhangsan");// 传递键值对参数
        Request request = new Request.Builder()// 创建Request 对象。
                .url(url).post(formBody.build())// 传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {// 回调的方法执行在子线程。
                }
            }
        });
    }
}
