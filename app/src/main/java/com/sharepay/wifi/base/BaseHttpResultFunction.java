package com.sharepay.wifi.base;

import com.sharepay.wifi.helper.LogHelper;

import io.reactivex.functions.Function;

public class BaseHttpResultFunction<T> implements Function<T, T> {
    @Override
    public T apply(T t) throws Exception {
        LogHelper.releaseLog("BaseHttpResultFunction apply data:" + t.toString());
        return t;
    }
}
