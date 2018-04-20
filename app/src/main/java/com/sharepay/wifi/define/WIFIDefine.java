package com.sharepay.wifi.define;

public class WIFIDefine {

    public static final String KEY_PREFERENCE_SPEEDDETECTIONURL = "key_preference_speedDetectionUrl";
    public static final String KEY_PREFERENCE_ISSHOWSTARTOVER = "key_preference_isshowstartover";

    public static final String APPID = "c7f1c318a82fd12dffe8bbd129037308";
    public static final String APPSECRET = "cd6722f5c749ec09acd749e7f11a189e";

    public static final String DB_NAME = "sharewifi.realm";

    public static final String ACTIVITY_JUMP_FROM = "activity_from";

    public interface WifiSignalStrength {
        int HIGH_MAX = 0;
        int HIGH_MIN = -50;
        int MEDIUM_MIN = -70;
        int WIFI_DISCONNECT = -200;
    }

    public interface HttpResultState {
        String SUCCESS = "1";
        String FAIL = "0";
    }

    public interface JUMP_ACTIVITY {
        String PERSONAL_CENTER = "personalcenter";
    }

    public interface JUMP_PAGE_REQUESTCODE {
        int JUMP_PAGE_REQUESTCODE = 110;
    }
}
