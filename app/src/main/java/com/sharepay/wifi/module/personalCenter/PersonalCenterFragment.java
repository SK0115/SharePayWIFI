package com.sharepay.wifi.module.personalCenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.costHistory.CostHistoryActivity;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.personalCenter.PersonalCenterActivity;
import com.sharepay.wifi.adapter.PersonalCenterAdapter;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.base.OnBaseItemClickListener;
import com.sharepay.wifi.baseCtrl.FullyLinearLayoutManager;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.model.http.AppVersionHttpData;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.info.PersonalCenterInfo;
import com.sharepay.wifi.model.realm.AccountInfoRealm;
import com.sharepay.wifi.util.CommonUtil;
import com.sharepay.wifi.util.FileManagerUtil;
import com.sharepay.wifi.util.HttpDownloader;
import com.sharepay.wifi.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalCenterFragment extends BaseFragment implements PersonalCenterContract.View {

    private static final String TAG = "PersonalCenterFragment ";
    private static final int PERSONAL_CENTER_HEAD_INDEX = 0;
    private static final int PERSONAL_CENTER_HISTORY_INDEX = 1;
    // private static final int PERSONAL_CENTER_CONTACTUS_INDEX = 2;
    // private static final int PERSONAL_CENTER_USERAGREEMENT_INDEX = 2;
    private static final int PERSONAL_CENTER_VERSIONINFO_INDEX = 2;
    private static final int PERSONAL_CENTER_EXIT_INDEX = 3;

    @BindView(R.id.recyclerview_personal_center)
    RecyclerView mPersonalCenterRecyclerview;

    private PersonalCenterContract.Presenter mPresenter;
    private PersonalCenterAdapter mAdapter;
    private List<PersonalCenterInfo> mPersonalCenterDataList;
    private boolean mIsLogin = false;
    private boolean mHasNewVersion = false;
    private int mVersionCode = 0;
    private String mVersionName;
    private AppVersionHttpData mAppVersionHttpData;

    @OnClick({ R.id.iv_personal_center_back })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_personal_center_back:
            mActivity.finish();
            break;
        default:
            break;
        }
    }

    private PersonalCenterActivity mActivity;

    public static PersonalCenterFragment getInstance() {
        PersonalCenterFragment mPersonalCenterFragment = new PersonalCenterFragment();
        Bundle bundle = new Bundle();
        mPersonalCenterFragment.setArguments(bundle);
        return mPersonalCenterFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void initView() {
        mVersionCode = CommonUtil.getVersionCode(mActivity);
        mVersionName = CommonUtil.getVersionName(mActivity);
        LogHelper.releaseLog(TAG + "initView mVersionCode:" + mVersionCode + " mVersionName:" + mVersionName);
        if (null != mPresenter) {
            mPresenter.requestAppVersion();
        }
        doLoginResult();
    }

    @Override
    protected void initData() {
        mActivity = (PersonalCenterActivity) getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.releaseLog(TAG + "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
        if (resultCode == WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_LOGIN_PAGE_REQUESTCODE) {
            doLoginResult();
        }
    }

    @Override
    public void setPresenter(PersonalCenterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setAppVersionHttpResult(BaseHttpResult<AppVersionHttpData> appVersionHttpResult) {
        if (null != appVersionHttpResult && null != appVersionHttpResult.getHttpData()
                && WIFIDefine.HttpResultState.SUCCESS.equals(appVersionHttpResult.getStatus())) {
            // 请求app升级信息成功
            mAppVersionHttpData = appVersionHttpResult.getHttpData();
            // mAppVersionHttpData.setVersion("1.0.2");
            try {
                String httpVersionCode = CommonUtil.getSplitString(mAppVersionHttpData.getVersion());
                int httpVersionFloat = Integer.valueOf(httpVersionCode);
                LogHelper.releaseLog(TAG + "setAppVersionHttpResult httpVersionFloat:" + httpVersionFloat + " mVersionCode:" + mVersionCode);
                if (httpVersionFloat > mVersionCode) {
                    if (!TextUtils.isEmpty(mAppVersionHttpData.getUrl())) {
                        downloadApk();
                    }
                    if (null != mPersonalCenterDataList && mPersonalCenterDataList.size() > 0) {
                        PersonalCenterInfo personalCenterData = mPersonalCenterDataList.get(PERSONAL_CENTER_VERSIONINFO_INDEX);
                        personalCenterData.setMessage(getString(R.string.click_update));
                        mPersonalCenterDataList.set(PERSONAL_CENTER_VERSIONINFO_INDEX, personalCenterData);
                        if (null != mAdapter) {
                            mAdapter.setDatas(mPersonalCenterDataList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } catch (Exception e) {
                LogHelper.errorLog(TAG + "setAppVersionHttpResult Exception! msg:" + e.getMessage());
            }
        }
    }

    /**
     * 处理登陆结果
     */
    private void doLoginResult() {
        final AccountInfoRealm accountInfoRealm = AccountHelper.getInstance().getAccountInfo();
        if (null != accountInfoRealm && !TextUtils.isEmpty(accountInfoRealm.getMobile()) && !TextUtils.isEmpty(accountInfoRealm.getId())) {
            mIsLogin = true;
        } else {
            mIsLogin = false;
        }
        doPersonalCenterDataList(accountInfoRealm);
        if (null == mAdapter) {
            mAdapter = new PersonalCenterAdapter(mActivity, mPersonalCenterDataList, false);
            mAdapter.setClickListener(mClickListener);
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<PersonalCenterInfo>() {
                @Override
                public void onItemClick(BaseHolder viewHolder, PersonalCenterInfo data, int position) {
                    if (getString(R.string.cost_history).equals(data.getTitle())) {
                        if (mIsLogin) {
                            Intent intent = new Intent(mActivity, CostHistoryActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phoneNum", accountInfoRealm.getMobile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(R.string.please_login);
                        }
                    }
                }
            });
            mPersonalCenterRecyclerview.setNestedScrollingEnabled(false);
            mPersonalCenterRecyclerview.setLayoutManager(new FullyLinearLayoutManager(mActivity));
            mPersonalCenterRecyclerview.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(mPersonalCenterDataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private PersonalCenterAdapter.PersonalCenterItemClickListener mClickListener = new PersonalCenterAdapter.PersonalCenterItemClickListener() {
        @Override
        public void click(String type) {
            if (PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT.equals(type)) {
                if (!mIsLogin) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(WIFIDefine.ACTIVITY_JUMP_FROM, WIFIDefine.JUMP_ACTIVITY.PERSONAL_CENTER);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_LOGIN_PAGE_REQUESTCODE);
                }
            } else if (PersonalCenterInfo.PERSONAL_CENTER_EXIT.equals(type)) {
                mIsLogin = false;
                AccountHelper.getInstance().logout();
                doLoginResult();
            } else if (PersonalCenterInfo.PERSONAL_CENTER_TEXT.equals(type)) {
                if (!mHasNewVersion) {
                    return;
                }
                boolean downloadSuccess = FileManagerUtil.getInstance().isDownloadSuccess();
                if (downloadSuccess) {
                    File file = FileManagerUtil.getInstance().getApkFile();
                    if (null == file) {
                        FileManagerUtil.getInstance().deleteFile();
                        if (null != mPresenter) {
                            mPresenter.requestAppVersion();
                        }
                    } else {
                        openFile(FileManagerUtil.getInstance().getApkFile());
                    }
                } else {
                    ToastUtils.showShort(R.string.apk_downloading);
                }
            }
        }
    };

    /**
     * 处理个人中心数据
     *
     * @param accountInfoRealm
     */
    private void doPersonalCenterDataList(AccountInfoRealm accountInfoRealm) {
        if (null == mPersonalCenterDataList) {
            mPersonalCenterDataList = new ArrayList<PersonalCenterInfo>();
        }
        mPersonalCenterDataList.clear();
        PersonalCenterInfo personalCenterData = new PersonalCenterInfo();
        if (mIsLogin && null != accountInfoRealm) {
            String mobile = accountInfoRealm.getMobile();
            if (mobile.length() == 11) {
                mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
            }
            personalCenterData.setTitle(mobile);
            personalCenterData.setMessage(accountInfoRealm.getIntegral() + "");
            personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT);
            mPersonalCenterDataList.add(PERSONAL_CENTER_HEAD_INDEX, personalCenterData);
        } else {
            personalCenterData.setImg(R.drawable.ic_account_img);
            personalCenterData.setMessage("0");
            personalCenterData.setTitle(getString(R.string.please_login));
            personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT);
            mPersonalCenterDataList.add(PERSONAL_CENTER_HEAD_INDEX, personalCenterData);
        }

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.cost_history));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(PERSONAL_CENTER_HISTORY_INDEX, personalCenterData);

        // 去除联系我们
        // personalCenterData = new PersonalCenterInfo();
        // personalCenterData.setTitle(getString(R.string.contact_us));
        // personalCenterData.setImg(R.drawable.ic_list_next);
        // personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        // mPersonalCenterDataList.add(PERSONAL_CENTER_CONTACTUS_INDEX,
        // personalCenterData);

        // personalCenterData = new PersonalCenterInfo();
        // personalCenterData.setTitle(getString(R.string.user_agreement));
        // personalCenterData.setImg(R.drawable.ic_list_next);
        // personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        // mPersonalCenterDataList.add(PERSONAL_CENTER_USERAGREEMENT_INDEX,
        // personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        String versionInfo = getString(R.string.version_info) + mVersionName;
        personalCenterData.setTitle(versionInfo);
        personalCenterData.setImg(0);
        personalCenterData.setMessage(getString(R.string.already_lastver));
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_TEXT);
        mPersonalCenterDataList.add(PERSONAL_CENTER_VERSIONINFO_INDEX, personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_EXIT);
        personalCenterData.setIsLogin(mIsLogin);
        mPersonalCenterDataList.add(PERSONAL_CENTER_EXIT_INDEX, personalCenterData);
    }

    public void downloadApk() {
        mHasNewVersion = true;
        List<String> permissionsList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.releaseLog(TAG + "initView no storage permission!");
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsList.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    WIFIDefine.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        } else {
            new DownloadApkThread().start();
        }
    }

    class DownloadApkThread extends Thread {
        @Override
        public void run() {
            super.run();
            HttpDownloader httpDownloader = new HttpDownloader();
            if (null != mAppVersionHttpData) {
                // httpDownloader.downloadFile("http://uv.aiseewhaley.aisee.tv/version/20180503/moretv_3.2.1_32100_201804282036_master_proguard_signed.apk",
                // "SharePayWifi.apk");
                httpDownloader.downloadFile(mAppVersionHttpData.getUrl(), "SharePayWifi.apk");
            }
        }
    }

    private void openFile(File file) {
        LogHelper.releaseLog(TAG + "openFile file:" + file);
        if (null == file) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File apkfile = new File(file.getPath());
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHasNewVersion = false;
        FileManagerUtil.getInstance().deleteFile();
    }
}
