package com.sharepay.wifi.helper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmHelper {

    private final String TAG = "RealmHelper ";
    private static RealmHelper mInstance; // 实例
    private Realm mRealm;

    public static RealmHelper getInstance() {
        synchronized (RealmHelper.class) {
            if (mInstance == null) {
                mInstance = new RealmHelper();
            }
        }
        return mInstance;
    }

    private RealmHelper() {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * add （增）
     */
    public void addRealmObject(RealmObject obj) {
        LogHelper.releaseLog(TAG + "addRealmObject obj:" + obj);
        try {
            if (!mRealm.isInTransaction()) {
                mRealm.beginTransaction();
            }
            mRealm.insert(obj);
            mRealm.commitTransaction();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "addRealmObject Exception! message:" + e.getMessage());
        }
    }

    /**
     * delete （删）
     */
    public void deleteRealmObject(RealmObject obj, String fieldName, String value) {
        LogHelper.releaseLog(TAG + "deleteRealmObject obj:" + obj + " fieldName:" + fieldName + " value:" + value);
        try {
            RealmObject realmObject = mRealm.where(obj.getClass()).equalTo(fieldName, value).findFirst();
            if (!mRealm.isInTransaction()) {
                mRealm.beginTransaction();
            }
            if (null != realmObject) {
                realmObject.deleteFromRealm();
            }
            mRealm.commitTransaction();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "deleteRealmObject Exception! message:" + e.getMessage());
        }
    }

    /**
     * 查询
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @return
     */
    public RealmObject queryRealmObjectByValue(RealmObject obj, String fieldName, String value) {
        LogHelper.releaseLog(TAG + "queryRealmObjectByValue obj:" + obj + " fieldName:" + fieldName + " value:" + value);
        try {
            return mRealm.where(obj.getClass()).equalTo(fieldName, value).findFirst();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "queryRealmObjectByValue Exception! message:" + e.getMessage());
        }
        return null;
    }

    /**
     * update
     */
    public void updateRealmObject(RealmObject obj) {
        LogHelper.releaseLog(TAG + "updateRealmObject obj:" + obj);
        try {
            if (!mRealm.isInTransaction()) {
                mRealm.beginTransaction();
            }
            mRealm.insertOrUpdate(obj);
            mRealm.commitTransaction();
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "updateRealmObject Exception! message:" + e.getMessage());
        }
    }

    /**
     * query （查询所有）
     */
    public List<RealmObject> queryAllShareWifiRealm(RealmObject obj, String fieldName) {
        LogHelper.releaseLog(TAG + "queryAllShareWifiRealm obj:" + obj + " fieldName:" + fieldName);
        try {
            RealmResults<RealmObject> realmObjects = (RealmResults<RealmObject>) mRealm.where(obj.getClass()).findAll();
            /**
             * 对查询结果，按fieldName进行排序，只能对查询结果进行排序
             */
            // 增序排列
            realmObjects = realmObjects.sort(fieldName);
            // //降序排列
            // realmObjects = realmObjects.sort(fieldName, Sort.DESCENDING);
            return mRealm.copyFromRealm(realmObjects);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "queryAllShareWifiRealm Exception! message:" + e.getMessage());
        }
        return null;
    }

    public boolean isRealmObjectExist(RealmObject obj, String fieldName, String value) {
        LogHelper.releaseLog(TAG + "isRealmObjectExist obj:" + obj + " fieldName:" + fieldName + " value:" + value);
        try {
            RealmObject realmObject = mRealm.where(obj.getClass()).equalTo(fieldName, value).findFirst();
            if (null != realmObject) {
                return true;
            }
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "isRealmObjectExist Exception! message:" + e.getMessage());
        }
        return false;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public void close() {
        LogHelper.releaseLog(TAG + "close！");
        // if (null != mRealm) {
        // mRealm.close();
        // }
    }
}
