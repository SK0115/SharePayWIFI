package com.sharepay.wifi.model.info;

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
    private String shareWifiId;
    private boolean isShared;
    private String earnings;

    public WIFIInfo(String name, String bssid, String capabilities, int signalStrength) {
        setName(name);
        setBSSID(bssid);
        setCapabilities(capabilities);
        setSignalStrength(signalStrength);
        setShareWifiId("-9999");
        setIsShared(false);
        setEarnings("-9999");
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

    public String getShareWifiId() {
        return shareWifiId;
    }

    public void setShareWifiId(String shareWifiId) {
        this.shareWifiId = shareWifiId;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        return "WIFIInfo={" + "name='" + getName() + '\'' + ", bssid='" + getBSSID() + '\'' + ", capabilities='" + getCapabilities() + '\''
                + ", signalStrength='" + getSignalStrength() + '\'' + ", shareWifiId='" + getShareWifiId() + '\'' + ", isShared='" + isShared() + '\''
                + ", earnings='" + getEarnings() + '\'' + '}';
    }

    private WIFIInfo(Parcel in) {
        name = in.readString();
        bssid = in.readString();
        capabilities = in.readString();
        signalStrength = in.readInt();
        shareWifiId = in.readString();
        isShared = in.readByte() != 0;
        earnings = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(bssid);
        dest.writeString(capabilities);
        dest.writeInt(signalStrength);
        dest.writeString(shareWifiId);
        dest.writeByte((byte) (isShared ? 1 : 0));
        dest.writeString(earnings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WIFIInfo> CREATOR = new Creator<WIFIInfo>() {
        @Override
        public WIFIInfo createFromParcel(Parcel in) {
            return new WIFIInfo(in);
        }

        @Override
        public WIFIInfo[] newArray(int size) {
            return new WIFIInfo[size];
        }
    };
}
