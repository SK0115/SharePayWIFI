package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 签到信息(数据库使用)
 */
public class SignInfoRealm extends RealmObject implements Parcelable {

    @PrimaryKey
    private String mobile;
    private String nickName;
    private String lastSignTimeString;
    private long lastSignTimeStamp;

    public SignInfoRealm() {
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

    public String getLastSignTimeString() {
        return lastSignTimeString;
    }

    public void setLastSignTimeString(String lastSignTimeString) {
        this.lastSignTimeString = lastSignTimeString;
    }

    public long getLastSignTimeStamp() {
        return lastSignTimeStamp;
    }

    public void setLastSignTimeStamp(long lastSignTimeStamp) {
        this.lastSignTimeStamp = lastSignTimeStamp;
    }

    @Override
    public String toString() {
        return "SignInfoRealm={" + "mobile='" + getMobile() + '\'' + ", nickName='" + getNickName() + '\'' + ", lastSignTimeString='" + getLastSignTimeString()
                + '\'' + ", lastSignTimeStamp='" + getLastSignTimeStamp() + '\'' + '}';
    }

    private SignInfoRealm(Parcel in) {
        mobile = in.readString();
        nickName = in.readString();
        lastSignTimeString = in.readString();
        lastSignTimeStamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobile);
        dest.writeString(nickName);
        dest.writeString(lastSignTimeString);
        dest.writeLong(lastSignTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignInfoRealm> CREATOR = new Creator<SignInfoRealm>() {
        @Override
        public SignInfoRealm createFromParcel(Parcel in) {
            return new SignInfoRealm(in);
        }

        @Override
        public SignInfoRealm[] newArray(int size) {
            return new SignInfoRealm[size];
        }
    };
}
