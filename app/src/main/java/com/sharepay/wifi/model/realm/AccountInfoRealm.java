package com.sharepay.wifi.model.realm;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 帐号信息(数据库)
 */
public class AccountInfoRealm extends RealmObject implements Parcelable {

    public static final String LOGIN_KEY = "loginKey";

    @PrimaryKey
    private String loginKey; // 设备信息key
    private String id;
    private String mobile;
    private String nickName;
    private String photo;
    private int integral;
    private String regTime;
    private String status;

    public AccountInfoRealm() {
        setLoginKey(LOGIN_KEY);
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

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
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountInfoRealm={" + "id='" + getId() + '\'' + ", mobile='" + getMobile() + '\'' + ", nickName='" + getNickName() + '\'' + ", photo='"
                + getPhoto() + '\'' + ", integral='" + getIntegral() + '\'' + ", regTime='" + getRegTime() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }

    private AccountInfoRealm(Parcel in) {
        loginKey = in.readString();
        id = in.readString();
        mobile = in.readString();
        nickName = in.readString();
        photo = in.readString();
        regTime = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loginKey);
        dest.writeString(id);
        dest.writeString(mobile);
        dest.writeString(nickName);
        dest.writeString(photo);
        dest.writeString(regTime);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountInfoRealm> CREATOR = new Creator<AccountInfoRealm>() {
        @Override
        public AccountInfoRealm createFromParcel(Parcel in) {
            return new AccountInfoRealm(in);
        }

        @Override
        public AccountInfoRealm[] newArray(int size) {
            return new AccountInfoRealm[size];
        }
    };

}
