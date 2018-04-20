package com.sharepay.wifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 个人中心数据
 */
public class PersonalCenterData implements Parcelable {

    public static final String PERSONAL_CENTER_TEXT = "personalCenterText";
    public static final String PERSONAL_CENTER_IMG = "personalCenterImg";
    public static final String PERSONAL_CENTER_ACCOUNT = "personalCenterAccount";
    public static final String PERSONAL_CENTER_EXIT = "personalCenterExit";

    private String title;
    private int img;
    private String message;
    private String type;
    private boolean isLogin;

    public PersonalCenterData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public String toString() {
        return "PersonalCenterData={" + "title='" + getTitle() + '\'' + ", img='" + getImg() + '\'' + ", message='" + getMessage() + '\'' + ", type='"
                + getType() + '\'' + ", isLogin='" + isLogin() + '\'' + '}';
    }

    private PersonalCenterData(Parcel in) {
        title = in.readString();
        img = in.readInt();
        message = in.readString();
        type = in.readString();
        isLogin = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(img);
        dest.writeString(message);
        dest.writeString(type);
        dest.writeByte((byte) (isLogin ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonalCenterData> CREATOR = new Creator<PersonalCenterData>() {
        @Override
        public PersonalCenterData createFromParcel(Parcel in) {
            return new PersonalCenterData(in);
        }

        @Override
        public PersonalCenterData[] newArray(int size) {
            return new PersonalCenterData[size];
        }
    };
}