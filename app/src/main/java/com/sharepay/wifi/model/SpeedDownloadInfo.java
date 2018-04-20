package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 测速请求数据
 */
public class SpeedDownloadInfo implements Parcelable {

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
        return "SpeedDownloadInfo={" + "speedTestUrl='" + getSpeedTestUrl() + '\'' + '}';
    }

    private SpeedDownloadInfo(Parcel in) {
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

    public static final Creator<SpeedDownloadInfo> CREATOR = new Creator<SpeedDownloadInfo>() {
        @Override
        public SpeedDownloadInfo createFromParcel(Parcel in) {
            return new SpeedDownloadInfo(in);
        }

        @Override
        public SpeedDownloadInfo[] newArray(int size) {
            return new SpeedDownloadInfo[size];
        }
    };
}
