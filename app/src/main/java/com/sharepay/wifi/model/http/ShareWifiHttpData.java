package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 共享wifi列表网络请求数据
 */
public class ShareWifiHttpData implements Parcelable {

    private String id;
    private String name;
    private String pass;
    private String ip;
    private String gateway;
    private String x_coordinate;
    private String y_coordinate;
    private String mobile;
    private String add_time;
    private String earnings;
    private String status;
    private String distanc;

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

    public String getAddtime() {
        return add_time;
    }

    public void setAddtime(String add_time) {
        this.add_time = add_time;
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

    @Override
    public String toString() {
        return "ShareWifiHttpData={" + "id='" + getId() + '\'' + ", name='" + getName() + '\'' + ", pass='" + getPass() + '\'' + ", ip='" + getIp() + '\''
                + ", gateway='" + getGateway() + '\'' + ", x_coordinate='" + getXCoordinate() + '\'' + ", y_coordinate='" + getYCoordinate() + '\''
                + ", mobile='" + getMobile() + '\'' + ", add_time='" + getAddtime() + '\'' + ", earnings='" + getEarnings() + '\'' + ", status='" + getStatus()
                + '\'' + ", distanc='" + getDistanc() + '\'' + '}';
    }

    private ShareWifiHttpData(Parcel in) {
        id = in.readString();
        name = in.readString();
        pass = in.readString();
        ip = in.readString();
        gateway = in.readString();
        x_coordinate = in.readString();
        y_coordinate = in.readString();
        mobile = in.readString();
        add_time = in.readString();
        earnings = in.readString();
        status = in.readString();
        distanc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(pass);
        dest.writeString(ip);
        dest.writeString(gateway);
        dest.writeString(x_coordinate);
        dest.writeString(y_coordinate);
        dest.writeString(mobile);
        dest.writeString(add_time);
        dest.writeString(earnings);
        dest.writeString(status);
        dest.writeString(distanc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareWifiHttpData> CREATOR = new Creator<ShareWifiHttpData>() {
        @Override
        public ShareWifiHttpData createFromParcel(Parcel in) {
            return new ShareWifiHttpData(in);
        }

        @Override
        public ShareWifiHttpData[] newArray(int size) {
            return new ShareWifiHttpData[size];
        }
    };
}
