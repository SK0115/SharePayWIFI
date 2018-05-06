package com.sharepay.wifi.model.realm;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 当前连接wifi信息(数据库)
 */
public class CurrentWifiInfoRealm extends RealmObject implements Parcelable {

    public static final String CONNECT_WIFI_KEY = "connectWifiKey";

    @PrimaryKey
    private String connectWifiKey; // 当前连接wifi信息key
    private String id; // 当前连接wifi的id
    private String name; // 当前连接wifi的名字
    private String mac; // 当前连接wifi的mac地址
    private String pass; // 当前连接wifi的密码
    private String ip; // 当前连接wifi的ip
    private String gateway; // 当前连接wifi的网关
    private String x_coordinate; // 当前连接wifi的纬度
    private String y_coordinate; // 当前连接wifi的经度
    private String mobile; // 当前连接wifi分享者的手机号
    private String add_time; // 当前连接wifi的分享时间
    private String earnings; // 当前连接wifi的收益
    private String status; // 当前连接wifi的状态
    private String distanc; // 当前连接wifi的距离
    private String capabilities; // 当前连接wifi的加密方案
    private int signalStrength; // 当前连接wifi的信号强度
    private boolean isShared; // 当前wifi是否为共享的
    private long connect_time; // 当前连接wifi的连接时间

    public CurrentWifiInfoRealm() {
        setConnectWifiKey(CONNECT_WIFI_KEY);
        setShared(false);
    }

    public String getConnectWifiKey() {
        return connectWifiKey;
    }

    public void setConnectWifiKey(String connectWifiKey) {
        this.connectWifiKey = connectWifiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getXCoordinate() {
        return x_coordinate;
    }

    public void setXCoordinate(String x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public String getYCoordinate() {
        return y_coordinate;
    }

    public void setYCoordinate(String y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddTime() {
        return add_time;
    }

    public void setAddTime(String add_time) {
        this.add_time = add_time;
    }

    public long getConnectTime() {
        return connect_time;
    }

    public void setConnectTime(long connect_time) {
        this.connect_time = connect_time;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistanc() {
        return distanc;
    }

    public void setDistanc(String distanc) {
        this.distanc = distanc;
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

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    @Override
    public String toString() {
        return "CurrentWifiInfoRealm={" + "id='" + getId() + '\'' + ", name='" + getName() + '\'' + ", mac='" + getMac() + '\'' + ", pass='" + getPass() + '\''
                + ", ip='" + getIp() + '\'' + ", gateway='" + getGateway() + '\'' + ", xcoordinate='" + getXCoordinate() + '\'' + ", ycoordinate='"
                + getYCoordinate() + '\'' + ", mobile='" + getMobile() + '\'' + ", add_time='" + getAddTime() + '\'' + ", connect_time='" + getConnectTime()
                + '\'' + ", earnings='" + getEarnings() + '\'' + ", status='" + getStatus() + '\'' + ", distanc='" + getDistanc() + '\'' + ", capabilities='"
                + getCapabilities() + '\'' + ", signalStrength='" + getSignalStrength() + '\'' + ", isShared='" + isShared() + '\'' + '}';
    }

    protected CurrentWifiInfoRealm(Parcel in) {
        connectWifiKey = in.readString();
        id = in.readString();
        name = in.readString();
        mac = in.readString();
        pass = in.readString();
        ip = in.readString();
        gateway = in.readString();
        x_coordinate = in.readString();
        y_coordinate = in.readString();
        mobile = in.readString();
        add_time = in.readString();
        connect_time = in.readLong();
        earnings = in.readString();
        status = in.readString();
        distanc = in.readString();
        capabilities = in.readString();
        signalStrength = in.readInt();
        isShared = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(connectWifiKey);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeString(pass);
        dest.writeString(ip);
        dest.writeString(gateway);
        dest.writeString(x_coordinate);
        dest.writeString(y_coordinate);
        dest.writeString(mobile);
        dest.writeString(add_time);
        dest.writeLong(connect_time);
        dest.writeString(earnings);
        dest.writeString(status);
        dest.writeString(distanc);
        dest.writeString(capabilities);
        dest.writeInt(signalStrength);
        dest.writeByte((byte) (isShared ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrentWifiInfoRealm> CREATOR = new Creator<CurrentWifiInfoRealm>() {
        @Override
        public CurrentWifiInfoRealm createFromParcel(Parcel in) {
            return new CurrentWifiInfoRealm(in);
        }

        @Override
        public CurrentWifiInfoRealm[] newArray(int size) {
            return new CurrentWifiInfoRealm[size];
        }
    };
}
