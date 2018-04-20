package com.sharepay.wifi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.sharepay.wifi.define.WIFIDefine;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SPApplication extends Application {
    private static Context mContext;
    public static List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name(WIFIDefine.DB_NAME).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static Context getContext() {
        return mContext;
    }

    public static void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }

    public static void clearLastActivity() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            activityList.remove(activityList.size() - 1);
        }
    }

    public static void clearExceptLastActivitys() {
        if (activityList == null || activityList.size() == 0) {

        } else {
            for (int i = 0; i < activityList.size() - 1; i++) {
                if (activityList.get(i) != null) {
                    activityList.get(i).finish();
                }
            }
            Activity activity = activityList.get(activityList.size() - 1);
            activityList.clear();
            activityList.add(activity);
        }
    }
}
