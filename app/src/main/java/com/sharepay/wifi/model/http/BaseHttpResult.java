package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 数据请求基础数据
 * 
 * @param <T>
 */
public class BaseHttpResult<T> implements Parcelable {

    private String status;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getHttpData() {
        return data;
    }

    public void setHttpData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseHttpResult={" + "status='" + getStatus() + '\'' + ", httpData='" + getHttpData().toString() + '\'' + '}';
    }

    private BaseHttpResult(Parcel in) {
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseHttpResult> CREATOR = new Creator<BaseHttpResult>() {
        @Override
        public BaseHttpResult createFromParcel(Parcel in) {
            return new BaseHttpResult(in);
        }

        @Override
        public BaseHttpResult[] newArray(int size) {
            return new BaseHttpResult[size];
        }
    };
}