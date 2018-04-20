package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 帐号信息
 */
public class LoginAccountHttpData implements Parcelable {

    private String id;
    private String mobile;
    private String nick_name;
    private String photo;
    private int integral;
    private String reg_time;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nick_name;
    }

    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getRegTime() {
        return reg_time;
    }

    public void setRegTime(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoginAccountHttpData={" + "id='" + getId() + '\'' + ", mobile='" + getMobile() + '\'' + ", nick_name='" + getNickName() + '\'' + ", photo='"
                + getPhoto() + '\'' + ", integral='" + getIntegral() + '\'' + ", reg_time='" + getRegTime() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }

    private LoginAccountHttpData(Parcel in) {
        id = in.readString();
        mobile = in.readString();
        nick_name = in.readString();
        photo = in.readString();
        reg_time = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mobile);
        dest.writeString(nick_name);
        dest.writeString(photo);
        dest.writeString(reg_time);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginAccountHttpData> CREATOR = new Creator<LoginAccountHttpData>() {
        @Override
        public LoginAccountHttpData createFromParcel(Parcel in) {
            return new LoginAccountHttpData(in);
        }

        @Override
        public LoginAccountHttpData[] newArray(int size) {
            return new LoginAccountHttpData[size];
        }
    };
}