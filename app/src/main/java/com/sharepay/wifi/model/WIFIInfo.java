package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * wifi信息
 */
public class WIFIInfo implements Parcelable {

    private String name;
    private String bssid;
    private String capabilities;
    private int signalStrength;

    public WIFIInfo(String name, String bssid, String capabilities, int signalStrength) {
        setName(name);
        setBSSID(bssid);
        setCapabilities(capabilities);
        setSignalStrength(signalStrength);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBSSID() {
        return bssid;
    }

    public void setBSSID(String bssid) {
        this.bssid = bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public String toString() {
        return "WIFIInfo={" + "name='" + getName() + '\'' + ", bssid='" + getBSSID() + '\'' + ", capabilities='" + getCapabilities() + '\''
                + ", signalStrength='" + getSignalStrength() + '\'' + '}';
    }

    private WIFIInfo(Parcel in) {
        this.name = in.readString();
        this.capabilities = in.readString();
        this.signalStrength = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.capabilities);
        dest.writeInt(this.signalStrength);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WIFIInfo> CREATOR = new Creator<WIFIInfo>() {
        @Override
        public WIFIInfo createFromParcel(Parcel source) {
            return new WIFIInfo(source);
        }

        @Override
        public WIFIInfo[] newArray(int size) {
            return new WIFIInfo[size];
        }
    };
}
