package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * token信息(数据库)
 */
public class TokenInfoRealm extends RealmObject implements Parcelable {

    public static final String TOKENINFO_KEY = "tokenInfo";

    @PrimaryKey
    private String tokenKey; // token信息key
    private String token; // 鉴权token
    private long timeStamp; // 时间戳
    private String timeString; // 时间字符

    public TokenInfoRealm() {
        setTokenKey(TOKENINFO_KEY);
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String toString() {
        return "TokenInfoRealm={" + "token='" + getToken() + '\'' + ", timeStamp='" + getTimeStamp() + '\'' + ", timeString='" + getTimeString() + '\'' + '}';
    }

    private TokenInfoRealm(Parcel in) {
        tokenKey = in.readString();
        token = in.readString();
        timeStamp = in.readLong();
        timeString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenKey);
        dest.writeString(token);
        dest.writeLong(timeStamp);
        dest.writeString(timeString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenInfoRealm> CREATOR = new Creator<TokenInfoRealm>() {
        @Override
        public TokenInfoRealm createFromParcel(Parcel in) {
            return new TokenInfoRealm(in);
        }

        @Override
        public TokenInfoRealm[] newArray(int size) {
            return new TokenInfoRealm[size];
        }
    };
}
