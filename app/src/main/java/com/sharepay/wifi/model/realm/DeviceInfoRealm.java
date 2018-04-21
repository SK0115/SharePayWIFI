package com.sharepay.wifi.model.realm;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 设备信息(数据库)
 */
public class DeviceInfoRealm extends RealmObject implements Parcelable {

    public static final String DEVICEINFO_KEY = "deviceUserID";

    @PrimaryKey
    private String deviceKey; // 设备信息key
    private String deviceID; // 设备id

    public DeviceInfoRealm() {
        setDeviceKey(DEVICEINFO_KEY);
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public String toString() {
        return "DeviceInfoRealm={" + "deviceID='" + getDeviceID() + '\'' + '}';
    }

    private DeviceInfoRealm(Parcel in) {
        deviceKey = in.readString();
        deviceID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceKey);
        dest.writeString(deviceID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceInfoRealm> CREATOR = new Creator<DeviceInfoRealm>() {
        @Override
        public DeviceInfoRealm createFromParcel(Parcel in) {
            return new DeviceInfoRealm(in);
        }

        @Override
        public DeviceInfoRealm[] newArray(int size) {
            return new DeviceInfoRealm[size];
        }
    };

}
