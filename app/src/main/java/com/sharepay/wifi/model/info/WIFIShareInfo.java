package com.sharepay.wifi.model.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * wifi共享信息
 */
public class WIFIShareInfo implements Parcelable {

    private String mobile; // 手机号
    private String name; // wifi名字
    private String pass; // wifi密码
    private String ip; // ip地址
    private String gateway; // 网关
    private String xcoordinate; // 纬度
    private String ycoordinate; // 经度
    private String earnings; // 收益

    public WIFIShareInfo() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        return xcoordinate;
    }

    public void setXCoordinate(String xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public String getYCoordinate() {
        return ycoordinate;
    }

    public void setYCoordinate(String ycoordinate) {
        this.ycoordinate = ycoordinate;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    @Override
    public String toString() {
        return "WIFIShareInfo={" + "mobile='" + getMobile() + '\'' + ", name='" + getName() + '\'' + ", pass='" + getPass() + '\'' + ", ip='" + getIp() + '\''
                + ", gateway='" + getGateway() + '\'' + ", xcoordinate='" + getXCoordinate() + '\'' + ", ycoordinate='" + getYCoordinate() + '\''
                + ", earnings='" + getEarnings() + '\'' + '}';
    }

    private WIFIShareInfo(Parcel in) {
        mobile = in.readString();
        name = in.readString();
        pass = in.readString();
        ip = in.readString();
        gateway = in.readString();
        xcoordinate = in.readString();
        ycoordinate = in.readString();
        earnings = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(pass);
        dest.writeString(ip);
        dest.writeString(gateway);
        dest.writeString(xcoordinate);
        dest.writeString(ycoordinate);
        dest.writeString(earnings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WIFIShareInfo> CREATOR = new Creator<WIFIShareInfo>() {
        @Override
        public WIFIShareInfo createFromParcel(Parcel in) {
            return new WIFIShareInfo(in);
        }

        @Override
        public WIFIShareInfo[] newArray(int size) {
            return new WIFIShareInfo[size];
        }
    };
}
