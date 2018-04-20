package com.sharepay.wifi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.sharepay.wifi.SPApplication;

import java.util.Map;
import java.util.Set;

public class PreferenceUtil {

    public static final String PREFS_NAME = "SP_WIFI";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPreferencesEditor;

    private static PreferenceUtil mPreferenceUtil;

    /**
     * 兼容appstore方法
     *
     * @return
     */
    public static PreferenceUtil getInstance() {
        if (null == mPreferenceUtil) {
            mPreferenceUtil = new PreferenceUtil();
        }
        return mPreferenceUtil;
    }

    /**
     * PreferenceManager构造函数
     * <p>
     * </p>
     *
     */
    public PreferenceUtil() {
        mPreferences = SPApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.apply();
    }

    /**
     * SP中写入String类型value
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void saveStringValue(String key, @Nullable String value) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putString(key, value).apply();
        }
    }

    /**
     * SP中读取String
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getStringValue(String key, String defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中写入int类型value
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void saveIntValue(String key, int value) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putInt(key, value).apply();
        }
    }

    /**
     * SP中读取int
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getIntValue(String key, int defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中写入long类型value
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void saveLongValue(String key, long value) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putLong(key, value).apply();
        }
    }

    /**
     * SP中读取long
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLongvalue(String key, long defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中写入float类型value
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void saveFloatValue(String key, float value) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putFloat(key, value).apply();
        }
    }

    /**
     * SP中读取float
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloatValue(String key, float defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void saveBooleanValue(String key, boolean value) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putBoolean(key, value).apply();
        }
    }

    /**
     * SP中读取boolean
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中写入String集合类型value
     *
     * @param key
     *            键
     * @param values
     *            值
     */
    public void saveStringSetValue(String key, @Nullable Set<String> values) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.putStringSet(key, values).apply();
        }
    }

    /**
     * SP中读取StringSet
     *
     * @param key
     *            键
     * @param defaultValue
     *            默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public Set<String> getStringSetValue(String key, @Nullable Set<String> defaultValue) {
        if (null != mPreferences) {
            return mPreferences.getStringSet(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAllValue() {
        if (null != mPreferences) {
            return mPreferences.getAll();
        }
        return null;
    }

    /**
     * SP中移除该key
     *
     * @param key
     *            键
     */
    public void removeValue(String key) {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.remove(key).apply();
        }
    }

    /**
     * SP中是否存在该key
     *
     * @param key
     *            键
     * @return {@code true}: 存在<br>
     *         {@code false}: 不存在
     */
    public boolean contains(String key) {
        if (null != mPreferences) {
            return mPreferences.contains(key);
        }
        return false;
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        if (null != mPreferencesEditor) {
            mPreferencesEditor.clear().apply();
        }
    }
}
