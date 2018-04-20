package com.sharepay.wifi.helper;

import android.text.TextUtils;

import com.sharepay.wifi.model.AccountInfoRealm;
import com.sharepay.wifi.model.BaseHttpResult;
import com.sharepay.wifi.model.LoginAccountHttpData;
import com.sharepay.wifi.model.SignInfoRealm;
import com.sharepay.wifi.util.CommonUtil;

import io.realm.RealmObject;

public class AccountHelper {

    private final String TAG = "AccountHelper ";

    private static AccountHelper mInstance; // 实例

    public static AccountHelper getInstance() {
        synchronized (AccountHelper.class) {
            if (mInstance == null) {
                mInstance = new AccountHelper();
            }
        }
        return mInstance;
    }

    /**
     * 将登陆帐号信息保存到数据库
     * 
     * @param loginAccountHttpData
     */
    public void addAccountInfoToRealm(BaseHttpResult<LoginAccountHttpData> loginAccountHttpData) {
        if (null != loginAccountHttpData && null != loginAccountHttpData.getHttpData()) {
            LogHelper.releaseLog(TAG + "addAccountInfo loginAccountHttpData:" + loginAccountHttpData.toString());
            AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
            accountInfoRealm.setId(loginAccountHttpData.getHttpData().getId());
            accountInfoRealm.setMobile(loginAccountHttpData.getHttpData().getMobile());
            accountInfoRealm.setNickName(loginAccountHttpData.getHttpData().getNickName());
            accountInfoRealm.setPhoto(loginAccountHttpData.getHttpData().getPhoto());
            accountInfoRealm.setIntegral(loginAccountHttpData.getHttpData().getIntegral());
            accountInfoRealm.setRegTime(loginAccountHttpData.getHttpData().getRegTime());
            accountInfoRealm.setStatus(loginAccountHttpData.getHttpData().getStatus());
            RealmHelper.getInstance().addRealmObject(accountInfoRealm);
        }
    }

    /**
     * 获取当前登陆信息
     * 
     * @return
     */
    public AccountInfoRealm getAccountInfo() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        RealmObject realmObject = RealmHelper.getInstance().queryRealmObjectByValue(accountInfoRealm, "loginKey", AccountInfoRealm.LOGIN_KEY);
        RealmHelper.getInstance().close();
        if (realmObject instanceof AccountInfoRealm) {
            accountInfoRealm = (AccountInfoRealm) realmObject;
            LogHelper.releaseLog(TAG + "getAccountInfo accountInfoRealm:" + accountInfoRealm.toString());
        } else {
            accountInfoRealm = null;
            LogHelper.errorLog(TAG + "getAccountInfo accountInfoRealm is null!");
        }
        return accountInfoRealm;
    }

    /**
     * 退出登陆
     */
    public void logout() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        if (RealmHelper.getInstance().isRealmObjectExist(accountInfoRealm, "loginKey", AccountInfoRealm.LOGIN_KEY)) {
            LogHelper.releaseLog(TAG + "logout!");
            RealmHelper.getInstance().deleteRealmObject(accountInfoRealm, "loginKey", AccountInfoRealm.LOGIN_KEY);
        }
    }

    /**
     * 获取当前登陆帐号的签到信息
     * 
     * @return
     */
    public SignInfoRealm getUserSignInfo() {
        AccountInfoRealm accountInfoRealm = getAccountInfo();
        if (null == accountInfoRealm) {
            LogHelper.errorLog(TAG + "getUserSignInfo accountInfoRealm is null!");
            return null;
        }
        SignInfoRealm signInfoRealm = new SignInfoRealm();
        RealmObject realmObject = RealmHelper.getInstance().queryRealmObjectByValue(signInfoRealm, "mobile", accountInfoRealm.getMobile());
        RealmHelper.getInstance().close();
        if (realmObject instanceof SignInfoRealm) {
            signInfoRealm = (SignInfoRealm) realmObject;
            LogHelper.releaseLog(TAG + "getUserSignInfo signInfoRealm:" + signInfoRealm.toString());
        } else {
            signInfoRealm = null;
            LogHelper.errorLog(TAG + "getUserSignInfo signInfoRealm is null!");
        }
        return signInfoRealm;
    }

    /**
     * 签到数据储存到数据库
     */
    public void addUserSignInfo() {
        SignInfoRealm signInfoRealm = getUserSignInfo();
        if (null != signInfoRealm && !TextUtils.isEmpty(signInfoRealm.getMobile())) {
            signInfoRealm.setLastSignTimeString(CommonUtil.getTimeFormat(System.currentTimeMillis(), ""));
            signInfoRealm.setLastSignTimeStamp(System.currentTimeMillis());
        } else {
            AccountInfoRealm accountInfoRealm = getAccountInfo();
            signInfoRealm = new SignInfoRealm();
            if (null != accountInfoRealm && !TextUtils.isEmpty(accountInfoRealm.getMobile()) && !TextUtils.isEmpty(accountInfoRealm.getId())) {
                signInfoRealm.setMobile(accountInfoRealm.getMobile());
                signInfoRealm.setNickName(accountInfoRealm.getNickName());
                signInfoRealm.setLastSignTimeString(CommonUtil.getTimeFormat(System.currentTimeMillis(), ""));
                signInfoRealm.setLastSignTimeStamp(System.currentTimeMillis());
            }
        }
        RealmHelper.getInstance().addRealmObject(signInfoRealm);
    }

    /**
     * 更新登陆用户信息
     * 
     * @param title
     *            需要更新的选项
     * @param content
     *            更新的内容
     */
    public void updataAccountInfo(String title, String content) {
        AccountInfoRealm accountInfoRealm = getAccountInfo();
        LogHelper.releaseLog(TAG + "updataAccountInfo Exist AccountInfoRealm:" + accountInfoRealm.toString());
        if (null != accountInfoRealm && !TextUtils.isEmpty(accountInfoRealm.getMobile()) && !TextUtils.isEmpty(accountInfoRealm.getId())) {
            int integral = accountInfoRealm.getIntegral();
            integral = integral + 2;
            LogHelper.releaseLog(TAG + "updataAccountInfo integral:" + integral);
            AccountInfoRealm infoRealm = new AccountInfoRealm();
            infoRealm.setId(accountInfoRealm.getId());
            infoRealm.setMobile(accountInfoRealm.getMobile());
            infoRealm.setNickName(accountInfoRealm.getNickName());
            infoRealm.setPhoto(accountInfoRealm.getPhoto());
            infoRealm.setIntegral(integral);
            infoRealm.setRegTime(accountInfoRealm.getRegTime());
            infoRealm.setStatus(accountInfoRealm.getStatus());
            LogHelper.releaseLog(TAG + "updataAccountInfo Update AccountInfoRealm:" + infoRealm.toString());
            RealmHelper.getInstance().updateRealmObject(infoRealm);
        }
    }

}
