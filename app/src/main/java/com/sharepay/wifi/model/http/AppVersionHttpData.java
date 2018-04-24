package com.sharepay.wifi.model.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * app升级信息
 */
public class AppVersionHttpData implements Parcelable {

    private String id;
    private String version;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AppVersionHttpData={" + "id='" + getId() + '\'' + ", version='" + getVersion() + '\'' + ", url='" + getUrl() + '\'' + '}';
    }

    private AppVersionHttpData(Parcel in) {
        id = in.readString();
        version = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(version);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppVersionHttpData> CREATOR = new Creator<AppVersionHttpData>() {
        @Override
        public AppVersionHttpData createFromParcel(Parcel in) {
            return new AppVersionHttpData(in);
        }

        @Override
        public AppVersionHttpData[] newArray(int size) {
            return new AppVersionHttpData[size];
        }
    };
}
