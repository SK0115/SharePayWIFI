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

    /**
     * 用户积分历史类型
     */
    public interface USER_INTEGRAL_HISTORY_TYPE {
        String TYPE_SIGN = "1";
        String TYPE_SHARE = "2";
        String TYPE_SPENDING = "3";
        String TYPE_SIGN_TEXT = "签到";
        String TYPE_SHARE_TEXT = "分享";
        String TYPE_SPENDING_TEXT = "消费";
    }

    /**
     * wifi分享参数
     */
    public interface WIFI_SHARE_PARAM {
        String KEY_MOBILE = "mobile";
        String KEY_NAME = "name";
        String KEY_PASS = "pass";
        String KEY_IP = "ip";
        String KEY_GATEWAY = "gateway";
        String KEY_X_COORDINATE = "x_coordinate";
        String KEY_Y_COORDINATE = "y_coordinate";
        String KEY_EARNINGS = "earnings";
    }

    public interface HttpRequestCallBack {

        /**
         * 请求结果
         * 
         * @param obj
         */
        void onNext(Object obj);

        /**
         * 错误信息
         * 
         * @param e
         */
        void onError(Throwable e);
    }
}
