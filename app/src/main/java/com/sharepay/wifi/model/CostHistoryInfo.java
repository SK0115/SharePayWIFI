package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消费历史信息
 */
public class CostHistoryInfo implements Parcelable {

    private String content; // 消费内容
    private String time; // 消费时间
    private String integration; // 消费积分

    public CostHistoryInfo(String content, String time, String integration) {
        setContent(content);
        setTime(time);
        setIntegration(integration);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String intergration) {
        this.integration = intergration;
    }

    @Override
    public String toString() {
        return "CostHistoryInfo={" + "content='" + getContent() + '\'' + ", time='" + getTime() + '\'' + ", integration='" + getIntegration() + '\'' + '}';
    }

    private CostHistoryInfo(Parcel in) {
        this.content = in.readString();
        this.time = in.readString();
        this.integration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.integration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CostHistoryInfo> CREATOR = new Creator<CostHistoryInfo>() {
        @Override
        public CostHistoryInfo createFromParcel(Parcel source) {
            return new CostHistoryInfo(source);
        }

        @Override
        public CostHistoryInfo[] newArray(int size) {
            return new CostHistoryInfo[size];
        }
    };
}
