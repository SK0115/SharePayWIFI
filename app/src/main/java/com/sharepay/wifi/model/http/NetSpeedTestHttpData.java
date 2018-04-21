package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 测速请求数据
 */
public class NetSpeedTestHttpData implements Parcelable {

    private String speedTestUrl;

    public String getSpeedTestUrl() {
        return speedTestUrl;
    }

    public void setSpeedTestUrl(String speedTestUrl) {
        if (null == speedTestUrl) {
            speedTestUrl = "";
        }
        this.speedTestUrl = speedTestUrl;
    }

    @Override
    public String toString() {
        return "NetSpeedTestHttpData={" + "speedTestUrl='" + getSpeedTestUrl() + '\'' + '}';
    }

    private NetSpeedTestHttpData(Parcel in) {
        speedTestUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(speedTestUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NetSpeedTestHttpData> CREATOR = new Creator<NetSpeedTestHttpData>() {
        @Override
        public NetSpeedTestHttpData createFromParcel(Parcel in) {
            return new NetSpeedTestHttpData(in);
        }

        @Override
        public NetSpeedTestHttpData[] newArray(int size) {
            return new NetSpeedTestHttpData[size];
        }
    };
}
