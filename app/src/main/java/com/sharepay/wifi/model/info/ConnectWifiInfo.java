package com.sharepay.wifi.model.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * wifi连接信息
 */
public class ConnectWifiInfo implements Parcelable {
    private String name;
    private boolean isLocked;

    public ConnectWifiInfo(String name, boolean isLocked) {
        this.name = name;
        this.isLocked = isLocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public String toString() {
        return "ConnectWifiInfo={" + "name='" + getName() + '\'' + ", isLocked='" + isLocked() + '\'' + '}';
    }

    private ConnectWifiInfo(Parcel in) {
        this.name = in.readString();
        this.isLocked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isLocked ? (byte) 1 : (byte) 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConnectWifiInfo> CREATOR = new Creator<ConnectWifiInfo>() {
        @Override
        public ConnectWifiInfo createFromParcel(Parcel source) {
            return new ConnectWifiInfo(source);
        }

        @Override
        public ConnectWifiInfo[] newArray(int size) {
            return new ConnectWifiInfo[size];
        }
    };
}
