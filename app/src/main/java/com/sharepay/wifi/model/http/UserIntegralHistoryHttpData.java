package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户积分历史
 */
public class UserIntegralHistoryHttpData implements Parcelable {

    private String id;
    private int integral;
    private String add_time;
    private String mobile;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getAddTime() {
        return add_time;
    }

    public void setAddTime(String add_time) {
        this.add_time = add_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserIntegralHistoryHttpData={" + "id='" + getId() + '\'' + ", integral='" + getIntegral() + '\'' + ", add_time='" + getAddTime() + '\''
                + ", mobile='" + getMobile() + '\'' + ", type='" + getType() + '\'' + '}';
    }

    private UserIntegralHistoryHttpData(Parcel in) {
        id = in.readString();
        integral = in.readInt();
        add_time = in.readString();
        mobile = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(integral);
        dest.writeString(add_time);
        dest.writeString(mobile);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserIntegralHistoryHttpData> CREATOR = new Creator<UserIntegralHistoryHttpData>() {
        @Override
        public UserIntegralHistoryHttpData createFromParcel(Parcel in) {
            return new UserIntegralHistoryHttpData(in);
        }

        @Override
        public UserIntegralHistoryHttpData[] newArray(int size) {
            return new UserIntegralHistoryHttpData[size];
        }
    };
}
