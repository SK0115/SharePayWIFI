package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * token请求数据
 */
public class TokenHttpData implements Parcelable {

    private String token;
    private String message;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TokenHttpData={" + "token='" + getToken() + '\'' + ", message='" + getMessage() + '\'' + '}';
    }

    private TokenHttpData(Parcel in) {
        token = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenHttpData> CREATOR = new Creator<TokenHttpData>() {
        @Override
        public TokenHttpData createFromParcel(Parcel in) {
            return new TokenHttpData(in);
        }

        @Override
        public TokenHttpData[] newArray(int size) {
            return new TokenHttpData[size];
        }
    };
}