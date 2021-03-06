package com.sharepay.wifi.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.sharepay.wifi.define.WIFIDefine;

public class LocationHelper {

    private final String TAG = "LocationHelper ";

    private WIFIDefine.LocationCallBack mLocationCallBack;
    private LocationManager mLocationManager;

    public void setLocationCallBack(WIFIDefine.LocationCallBack callBack) {
        mLocationCallBack = callBack;
    }

    public void location(Context context) {
        LogHelper.releaseLog(TAG + "location!");
        try {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);// 低精度，如果设置为高精度，依然获取不了location。
            criteria.setAltitudeRequired(false);// 不要求海拔
            criteria.setBearingRequired(false);// 不要求方位
            criteria.setCostAllowed(true);// 允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗

            // 从可用的位置提供器中，匹配以上标准的最佳提供器
            String locationProvider = mLocationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = mLocationManager.getLastKnownLocation(locationProvider);
            LogHelper.releaseLog(TAG + "location:" + location);
            setLocation(location);
            // 监视地理位置变化
            mLocationManager.requestLocationUpdates(locationProvider, 5000, 5.0f, mLocationListener);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "location Exception! msg:" + e.getMessage());
            setLocation(null);
        }
    }

    public void release() {
        LogHelper.releaseLog(TAG + "release!");
        if (null != mLocationManager) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            mLocationManager = null;
        }
        mLocationCallBack = null;
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogHelper.releaseLog(TAG + "onProviderEnabled:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogHelper.releaseLog(TAG + "onProviderDisabled:" + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            LogHelper.releaseLog(TAG + "onLocationChanged!");
            // 如果位置发生变化,重新显示
            setLocation(location);
        }
    };

    private void setLocation(Location location) {
        // latitude 纬度
        // longitude 经度
        if (null != location) {
            LogHelper.releaseLog(TAG + "setLocation success!" + " latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
        }
        if (null != mLocationCallBack) {
            mLocationCallBack.setLocation(location);
        }
    }
}
