package com.sharepay.wifi.helper;

import android.util.Log;

public class LogHelper {

    private static final String LOGTAG = "WIFISHARE";

    public static void releaseLog(String log) {
        Log.i(LOGTAG, log);
    }

    public static void errorLog(String log) {
        Log.e(LOGTAG, log);
    }
}
