package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 基础请求数据
 */
public class BaseHttpData implements Parcelable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseHttpData={" + "message='" + getMessage() + '\'' + '}';
    }

    private BaseHttpData(Parcel in) {
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseHttpData> CREATOR = new Creator<BaseHttpData>() {
        @Override
        public BaseHttpData createFromParcel(Parcel in) {
            return new BaseHttpData(in);
        }

        @Override
        public BaseHttpData[] newArray(int size) {
            return new BaseHttpData[size];
        }
    };
}