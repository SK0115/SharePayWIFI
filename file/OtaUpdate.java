package com.peersless.api.ota;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.domaindetection.client.DomainClient;
import com.domaindetection.client.net.INetHandler;
import com.domaindetection.client.net.RequestInfo;
import com.domaindetection.client.net.RequestListener;
import com.domaindetection.client.net.ResultInfo;
import com.domaindetection.define.HttpCode;
import com.peersless.api.LauncherCore;
import com.peersless.api.config.Config;
import com.peersless.api.config.UserInfo;
import com.peersless.api.constant.EventType;
import com.peersless.api.constant.MoretvEvent;
import com.peersless.api.ota.OtaUpdateClient.RemindType;
import com.peersless.api.util.MD5Util;
import com.peersless.api.util.MidLog;
import com.peersless.api.util.NetWorkUtil;
import com.peersless.api.util.ProductInfo;

public class OtaUpdate {

	private static final int STATE_SUCCESS = 0;
	private static final int STATE_ERROR = 1;
	private static OtaUpdate mOtaUpdate;
	private static final String TAG = "Mid-Update";

	private Context mContext;
	private int mDownloadProgress = 0;
	private String mUpdateParams;
	private boolean mDownloadResult = false;

	private Timer mTimer;
	private TimerTask mTimerTask;
	
	private boolean checked = false;
	private boolean inDownload = false;
	private boolean uiCheckFlag = false;
	
	//update information.
	private static final String PREF_NAME = "UpdateCheck";
	private static final String KEY_HAS_NEW = "hasNew";
	private static final String KEY_FILEPATH = "filePath";
	private static final String KEY_VERSION = "apkVersion";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_UPDATE_TYPE = "updateType";
	private static final String KEY_FILE_HASH = "fileHash";
	private static final String KEY_NO_REMIND = "noRemind";
	private static final String KEY_NO_REMIND_VER = "noRemindVersion";
	
	
	private int mDownloadErrorType = 0;
	private static int ERROR_TYPE_URL_ERROR = 100;
	private static int ERROR_TYPE_FILE_NOT_FOUNT = 100;
	private static int ERROR_TYPE_MD5_CHECK_FALSE = 200;
	private static int ERROR_TYPE_NORMAL = 300;

		
	private static OtaMsg checkResult = OtaMsg.NO_CHECKED;
		
	private static final String UTF8 = "UTF-8";
	
	private static String mIp;
	private static String mDesc;
	private static String mDeviceId;
	private static String mAoc;
	private static String mChannel;
	private static int mVersionCode;

	private OtaUpdate() {}

	public static OtaUpdate getInstance() {
		if (mOtaUpdate == null) {
			mOtaUpdate = new OtaUpdate();
		}
		return mOtaUpdate;
	}
	
	boolean inited = false;
	public void init(Context context,UserInfo userInfo,String params){
		if(!inited){
			inited = true;
			this.mContext = context;
			MidLog.d(TAG, "init");
			if(userInfo != null){
				OtaInfo.getInstance().setApkVersion_client(userInfo.getVersionName());
				OtaInfo.getInstance().setApkSeries(userInfo.getSerialNum());
			}
			OtaInfo.getInstance().setMacaddress(NetWorkUtil.getMacAddress(mContext));
			
			this.mUpdateParams = params;
//			startAutoCheck();			
		}
	}
	
	public static String mapParamsToStr(Map<String, String> map){
		if (map == null || map.size() == 0) {
			return null;
		}
		String updateParams = "";
		StringBuffer sb = new StringBuffer();
		try {
			for (String key : map.keySet()) {
				sb.append("&")
				  .append(key)
				  .append("=")
				  .append(URLEncoder.encode(map.get(key), UTF8));
			}
			updateParams += sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return updateParams.toString();
	}

	public void unInit() {
		if(mTimer != null)
			mTimer.cancel();
	}
	
    public int getDownloadProgress() {
        MidLog.d(TAG, "mDownloadProgress : " + mDownloadProgress);
        return mDownloadProgress;
    }

    public OtaMsg checkUpdate() {
        checkResult = CheckServerNewVersion();
        return checkResult;
    }

	/**
	 * 升级描述信息
	 */
	public String getUpdateDescription() {
		return OtaInfo.getInstance().getApkNewContent();
	}

	public String getVersionInServer() {
		return OtaInfo.getInstance().getApkVersion_server();
	}
	
	public int getVersionCode(){
	    return OtaInfo.getInstance().getVersionCode();
	}

	public void installApk() {
		MidLog.d(TAG, "start install apk");
        try {
            File filePath = mContext.getFilesDir();
            Process process = Runtime.getRuntime().exec("chmod 777 " + filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                MidLog.d(TAG, "burrerfer: " + line);
            }
        } catch (Exception e) {
            MidLog.d(TAG, "chmod error : " + e);
        }
		SharedPreferences share = mContext.getSharedPreferences(PREF_NAME, 0);
		share.edit().putBoolean(OtaUpdate.KEY_HAS_NEW, false).commit();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File apkfile = new File(mContext.getFilesDir() + "/MoreTV.apk");
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	
	public void setRemindType(int type){
		MidLog.d(TAG, "set remind type : " + type);
		if(type == RemindType.TYPE_NO_REMIND_THIS_VERSION.getTypeNum()){
			setThisVersionNoRemind();
		}else if(type == RemindType.TYPE_REMIND_LATER.getTypeNum()){
			setRemindLater();
		}
	}
	
    /**
     * 设置检测升级所需的额外数据
     * 
     * @param ip
     *            ip地址
     * @param desc
     *            城市屏蔽级别
     * @param deviceId
     *            设备唯一识别
     * @param aoc
     *            城市编码
     * @param channel
     *            渠道号
     * 
     */
	public void setExtraInfo(String ip, String desc, String deviceId, String aoc, String channel, int versionCode){
	    mIp = ip;
	    mDesc = desc;
	    mDeviceId = deviceId;
	    mAoc = aoc;
	    mChannel = channel;
	    mVersionCode = versionCode;
	}

	private void setRemindLater() {
		if(mTimer != null)
			mTimer.cancel();
	}
	
	//设置不在提醒。
	private void setThisVersionNoRemind() {
		SharedPreferences share = mContext.getSharedPreferences(PREF_NAME, 0);
		Editor edit = share.edit();
		edit.putBoolean(KEY_NO_REMIND, true);
		edit.putString(KEY_NO_REMIND_VER, OtaInfo.getInstance().getApkVersion_server());
        edit.commit();
	}
	
	public void startAutoCheck() {
		// 初始化升级信息
		SharedPreferences share = mContext.getSharedPreferences(PREF_NAME, 0);
		OtaInfo.getInstance().setApkVersion_server(share.getString(KEY_VERSION, ""));
		OtaInfo.getInstance().setApkPath(share.getString(KEY_FILEPATH, ""));
		OtaInfo.getInstance().setApkNewContent(share.getString(KEY_DESCRIPTION, ""));
		OtaInfo.getInstance().setUpdateType(share.getInt(KEY_UPDATE_TYPE, 0));
		OtaInfo.getInstance().setFileHash(share.getString(KEY_FILE_HASH, ""));

		startTimer(10);//改为延迟10秒启动定时器。
	} 

	private void startTimer(long delay) {
		mTimer = new Timer();
		mTimerTask = new AutoCheckTask();
		mTimer.schedule(mTimerTask, delay * 1000, 1000 * 60 * 60);
		Log.i(TAG, "timer start");
	}
	
	private class AutoCheckTask extends TimerTask {
		public void run() {
			checkResult = CheckServerNewVersion();
			if (checkResult == OtaMsg.CHECK_RESULT_HAS_NEW || checkResult == OtaMsg.CHECK_RESULT_HAS_FOCUS_UPDATE) {
				downloadApk();
			}
		}
	}
	
	public void download(){
		MidLog.d(TAG, "ui call download");
		uiCheckFlag = true;
		if(!inDownload){
			new Thread(new Runnable() {
				public void run() {
					downloadApk();				
				}
			}).start();			
		} else {
			MidLog.d(TAG, "already in download");
		}
	}
	
	private void downloadApk(){
		if (!inDownload) {
			inDownload = true;
			boolean downResult = DownloadServerVersion();
			reportDownloadResult(downResult);
			inDownload = false;
		}
	}

	private void reportDownloadResult(boolean downResult) {
		MidLog.d(TAG, "report Download Result : " + downResult);
		if(uiCheckFlag && downResult){
			uiCheckFlag = false;
            switch (checkResult) {
            case CHECK_RESULT_HAS_NEW:
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota, EventType.OtaUpdateType.DOWNLOADED_SUCCESS_NORMAL,
                        null);
                break;
            case CHECK_RESULT_HAS_REMIND_VERSION:
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota,
                        EventType.OtaUpdateType.DOWNLOADED_SUCCESS_NO_MORE_REMIND, null);
                break;
            case CHECK_RESULT_HAS_FOCUS_UPDATE:
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota,
                        EventType.OtaUpdateType.DOWNLOADED_SUCCESS_FOCUS_INSDTALL, null);
                break;
            default:
                break;
            }
		} else if(uiCheckFlag && !downResult){
			uiCheckFlag = false;
			if(mDownloadErrorType == ERROR_TYPE_FILE_NOT_FOUNT || mDownloadErrorType == ERROR_TYPE_URL_ERROR){
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota, EventType.OtaUpdateType.DOWNLOAD_FALSE_URL_ERR, null);
			} else if(mDownloadErrorType == ERROR_TYPE_MD5_CHECK_FALSE){
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota,
                        EventType.OtaUpdateType.DOWNLOAD_FALSE_MD5_CHECK_FALSE, null);
			} else {
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota, EventType.OtaUpdateType.DOWNLOAD_FALSE, null);
			} 
		} else if(!uiCheckFlag && downResult){
			uiCheckFlag = false;
			switch(checkResult){
			case CHECK_RESULT_HAS_NEW:
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota, EventType.OtaUpdateType.DOWNLOADED_SUCCESS_NORMAL,
                        null);
				break;
			case CHECK_RESULT_HAS_FOCUS_UPDATE:
                LauncherCore.onMidCallback(MoretvEvent.Event_Ota,
                        EventType.OtaUpdateType.DOWNLOADED_SUCCESS_FOCUS_INSDTALL, null);
				break;
			default:
				break;
			}	
		}
	}

	private boolean DownloadServerVersion() {
		mDownloadErrorType = 0;
		MidLog.d(TAG, "start download");
		mDownloadProgress = 0;
		File apkfile = new File(mContext.getFilesDir() + "/MoreTV.apk");
		if (apkfile.exists()) {
			if (isMD5OK()) {
				mDownloadProgress = 100;
				MidLog.d(TAG, "already download");
				return true;
			} else {
				apkfile.delete();
			}
		}

		String request = OtaInfo.getInstance().getApkPath();
		if (request == null || request.length() < 10) {
			MidLog.e(TAG, "GET  apk url: is null or empty");
			mDownloadErrorType = ERROR_TYPE_URL_ERROR;
			return false;
		}
		MidLog.d(TAG, "apk url:" + request);
		startDownloadApk(request);
		return mDownloadResult;
	}

	private void startDownloadApk(String apkUrl){
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.parseVisiableUrl(apkUrl);
		new DomainClient(mNetHandler, new RequestListener() {
			@Override
			public void onFinish(ResultInfo resultInfo) {
				if (null != resultInfo && STATE_SUCCESS == resultInfo.getStateCode()){
					if(mDownloadErrorType != 0){
						mDownloadResult = false;
					}
					if (isMD5OK()) {
						MidLog.d(TAG, " download success");
						mDownloadProgress = 100;
						mDownloadResult = true;
					} else {
						mDownloadErrorType = ERROR_TYPE_MD5_CHECK_FALSE;
						MidLog.d(TAG, " download false , md5 check un pass");
						mDownloadProgress = 97;
						mDownloadResult = false;
					}
				}else{
					mDownloadResult = false;
				}
			}
		}).request(requestInfo);
	}

	private INetHandler mNetHandler = new INetHandler() {
		@Override
		public int handle(RequestInfo requestInfo, ResultInfo resultInfo) {
			if (null == requestInfo || null == resultInfo){
				return HttpCode.STATE_ERROR;
			}

			if (!requestInfo.isVisibleDomain()){
				if (TextUtils.isEmpty(requestInfo.getExtraDomain())){
					resultInfo.setStateCode(STATE_ERROR);
					return HttpCode.STATE_ERROR;
				}
			}

			InputStream is = null;
			FileOutputStream outStream = null;
			try {
				MidLog.d(TAG, "enter try url = " + requestInfo.getRequestUrl());
				URL url = new URL(requestInfo.getRequestUrl());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//设置指定header
				Map<String,String> headerMap = requestInfo.getHeadParams();
				if(null != headerMap && !headerMap.isEmpty()){
					for(Entry<String,String> entry : headerMap.entrySet()){
						conn.setRequestProperty(entry.getKey(), entry.getValue());
					}
				}
				conn.setConnectTimeout(10 * 1000);
				conn.setReadTimeout(10 * 1000);
				conn.connect();
				long length = conn.getContentLength();
				MidLog.i(TAG, "server len:" + length);

				is = conn.getInputStream();
				outStream = mContext.openFileOutput("MoreTV.apk",
						Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);

				long count = 0;
				int numRead;
				byte buf[] = new byte[1536];// MTU max //更Http有关

				int tmp = 0;
				while ((numRead = is.read(buf)) >= 0) {
					outStream.write(buf, 0, numRead);
					outStream.flush();
					count += numRead;

					// 更新进度
					mDownloadProgress = (int) (count * 95 / length);
					if(tmp != mDownloadProgress && mDownloadProgress % 5 == 0 ) {
						MidLog.d(TAG,"download count: " + count + " ; length : " + length + " ; progress : " + mDownloadProgress);
						tmp = mDownloadProgress;
					}
					if (mDownloadProgress >= 95) {
						mDownloadProgress = 95;
					}
				}
				MidLog.d(TAG,"download count: " + count + " ; length : " + length + " ; progress : " + mDownloadProgress);
			} catch (FileNotFoundException e) {
				mDownloadErrorType = ERROR_TYPE_FILE_NOT_FOUNT;
				MidLog.e(TAG, "download exception " + e);
				resultInfo.setStateCode(STATE_ERROR);
				return HttpCode.STATE_ERROR;
			} catch (Exception e) {
				mDownloadErrorType = ERROR_TYPE_NORMAL;
				MidLog.e(TAG, "download exception " + e);
				resultInfo.setStateCode(STATE_ERROR);
				return HttpCode.STATE_ERROR;
			} finally {
				safeClose(outStream);
				safeClose(is);
			}
			resultInfo.setStateCode(STATE_SUCCESS);
			return HttpCode.STATE_SUCCESS;
		}
	};
	
	private void safeClose(Closeable closeObj) {
		try {
			if (closeObj != null)
				closeObj.close();
		} catch (Exception e) {
		}
	}
	
	public OtaMsg getCheckResult(){
		return checkResult;
	}
	
	public boolean getIfHasBootUpgrade(){
		return "true".equals(OtaInfo.getInstance().getBootUpgrade());
	}
	
	public String getUpgradeImage(){
		return OtaInfo.getInstance().getUpgradeImage();
	}

    public String getUpgradeImageMd5() {
        return OtaInfo.getInstance().getUpgradeImageMd5();
    }

	private OtaMsg CheckServerNewVersion() {
		OtaMsg result = OtaMsg.NO_CHECKED;
		int isUpdate = -1;
		try {
			String httpUrl = getUpdateCheckUrl();
			HttpGet httpGet = new HttpGet(httpUrl);
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);// 设置链接超时时间.
			HttpResponse response = httpClient.execute(httpGet);
			StringBuilder builder = new StringBuilder();
			InputStream is = response.getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(is,"utf-8"));
			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
				builder.append(s);
			}
			
			Log.d(TAG, builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONObject jsonData = null;
			if(jsonObject.has("data")){
				jsonData = jsonObject.getJSONObject("data");
			}else {
				result = OtaMsg.CHECK_RESULT_FALSE;
				return result;
			}
			
			// 获取服务端是否更新
			if (jsonData.has("isUpdate")) {
				isUpdate = Integer.parseInt(jsonData.getString( "isUpdate").trim());
				OtaInfo.getInstance().setIsUpdate(isUpdate);
			}

			if (isUpdate == 1) {
				if (jsonData.has("apkVersion")) {
					OtaInfo.getInstance().setApkVersion_server(jsonData.getString("apkVersion").trim());
				}
				if (jsonData.has("filePath")) {
					OtaInfo.getInstance().setApkPath(jsonData.getString("filePath"));
				}
				if (jsonData.has("description")) {
					OtaInfo.getInstance().setApkNewContent(jsonData.getString("description"));
				}
				// 获取服务端升级类型
				if (jsonData.has("updateType")) {
					OtaInfo.getInstance().setUpdateType(Integer.parseInt(jsonData
							.getString("updateType").trim()));
				}
				// 获取apk文件MD5值
				if (jsonData.has("fileHash")) {
					OtaInfo.getInstance().setFileHash(jsonData.getString("fileHash")
							.toLowerCase());
				}

				if (jsonData.has("bootUpgrade")) {
					OtaInfo.getInstance().setBootUpgrade(jsonData.getString("bootUpgrade"));
				}

				if (jsonData.has("upgradeImage")) {
					OtaInfo.getInstance().setUpgradeImage(jsonData.getString("upgradeImage"));
				}

				if (jsonData.has("upgradeImageMd5")) {
                    OtaInfo.getInstance().setUpgradeImageMd5(jsonData.getString("upgradeImageMd5"));
                }
				
				OtaInfo.getInstance().setVersionCode(jsonData.optInt("baseVersionId"));
				result = OtaMsg.CHECK_RESULT_HAS_NEW;
			} else {
				if (jsonData.has("description")) {
					OtaInfo.getInstance().setApkNewContent(jsonData
							.getString("description"));
				}
				result = OtaMsg.CHECK_RESULT_NO_NEW;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			result = OtaMsg.CHECK_RESULT_FALSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = OtaMsg.CHECK_RESULT_FALSE;
		} catch (IOException e) {
			e.printStackTrace();
			result = OtaMsg.CHECK_RESULT_FALSE;
		}

		SharedPreferences share = mContext.getSharedPreferences(PREF_NAME, 0);
		if (result == OtaMsg.CHECK_RESULT_HAS_NEW) {
			Editor edit = share.edit();
			edit.putBoolean(KEY_HAS_NEW, true);
			edit.putString(KEY_VERSION, OtaInfo.getInstance().getApkVersion_server());
			edit.putString(KEY_FILEPATH, OtaInfo.getInstance().getApkPath());
			edit.putString(KEY_DESCRIPTION, OtaInfo.getInstance().getApkNewContent());
			edit.putInt(KEY_UPDATE_TYPE, OtaInfo.getInstance().getUpdateType());
			edit.putString(KEY_FILE_HASH, OtaInfo.getInstance().getFileHash());
			edit.commit();

			String str = share.getString(KEY_NO_REMIND_VER, "");
			boolean isHasReminded = OtaInfo.getInstance().getApkVersion_server().equals(str);

			if(OtaInfo.getInstance().getUpdateType() == 1){
				//强制升级
				result = OtaMsg.CHECK_RESULT_HAS_FOCUS_UPDATE;
			} else if(isHasReminded){
				result = OtaMsg.CHECK_RESULT_HAS_REMIND_VERSION;				
			}
		}
		checked = true;
		// reportCheckResult(result);
		return result;
	}
	
	private String getUpdateCheckUrl() throws UnsupportedEncodingException {
		StringBuffer sb1 = new StringBuffer();
		String url = "";
		url = sb1.append(Config.getOTA_API())
					.append("version=").append(OtaInfo.getInstance().getApkVersion_client())
					.append("&mac=").append(OtaInfo.getInstance().getMacaddress())
					.append("&series=").append(OtaInfo.getInstance().getApkSeries())
					.append("&ProductModel=").append(URLEncoder.encode(ProductInfo.getProductModel() ,UTF8))
					.append("&ProductSerial=").append(URLEncoder.encode(ProductInfo.getProductSerial(),UTF8))
					.append("&ProductVersion=").append(URLEncoder.encode(ProductInfo.getProductVersion(),UTF8))
					.append("&WifiMac=").append(NetWorkUtil.getWifiMac(mContext))
					.append("&ip=").append(mIp)
					.append("&desc=").append(mDesc)
					.append("&deviceId=").append(mDeviceId)
					.append("&aoc=").append(mAoc)
					.append("&promotionChannelCode=").append(mChannel)
					.append("&versionCode=").append(mVersionCode)
					.toString();
		if(mUpdateParams != null){
			url += mUpdateParams;
		}
		
		Log.d(TAG, "url: " + url);
		return url;
	}

	private boolean isMD5OK() {
		File apkfile = new File(mContext.getFilesDir() + "/MoreTV.apk");
		String serMD5 = OtaInfo.getInstance().getFileHash();

		boolean result = false;
		if (!apkfile.exists()) {
			MidLog.w(TAG, "HAS_NEW_VERSION_MD5_FAILURE : FILE IS NOT EXIT");
			return false;
		}

		try {
			String apkMD5 = MD5Util.getFileMD5String(apkfile);
			if (apkMD5.equalsIgnoreCase(serMD5)) {
				result = true;
			} else {
				Log.i(TAG, "apk MD5 in server is  " + serMD5);
				Log.i(TAG, "apk downloaded md5 is   " + apkMD5);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}
}
