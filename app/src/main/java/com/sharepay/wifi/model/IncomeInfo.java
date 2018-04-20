package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 共享收入信息
 */
public class IncomeInfo implements Parcelable {

    private String content;
    private String integration;

    public IncomeInfo(String content, String integration) {
        setContent(content);
        setIntegration(integration);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String intergration) {
        this.integration = intergration;
    }

    @Override
    public String toString() {
        return "IncomeInfo={" + "content='" + getContent() + '\'' + ", integration='" + getIntegration() + '\'' + '}';
    }

    private IncomeInfo(Parcel in) {
        this.content = in.readString();
        this.integration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.integration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IncomeInfo> CREATOR = new Creator<IncomeInfo>() {
        @Override
        public IncomeInfo createFromParcel(Parcel source) {
            return new IncomeInfo(source);
        }

        @Override
        public IncomeInfo[] newArray(int size) {
            return new IncomeInfo[size];
        }
    };
}
