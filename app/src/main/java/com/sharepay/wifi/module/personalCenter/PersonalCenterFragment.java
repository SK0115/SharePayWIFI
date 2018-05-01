package com.sharepay.wifi.module.personalCenter;

import android.content.Intent;
import android.os.Bundle;
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
import com.sharepay.wifi.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalCenterFragment extends BaseFragment implements PersonalCenterContract.View {

    private static final String TAG = "PersonalCenterFragment ";
    private static final int PERSONAL_CENTER_HEAD_INDEX = 0;
    private static final int PERSONAL_CENTER_HISTORY_INDEX = 1;
    private static final int PERSONAL_CENTER_CONTACTUS_INDEX = 2;
    private static final int PERSONAL_CENTER_USERAGREEMENT_INDEX = 3;
    private static final int PERSONAL_CENTER_VERSIONINFO_INDEX = 4;
    private static final int PERSONAL_CENTER_EXIT_INDEX = 5;

    @BindView(R.id.recyclerview_personal_center)
    RecyclerView mPersonalCenterRecyclerview;

    private PersonalCenterContract.Presenter mPresenter;
    private PersonalCenterAdapter mAdapter;
    private List<PersonalCenterInfo> mPersonalCenterDataList;
    private boolean mIsLogin = false;
    private int mVersionCode = 0;
    private String mVersionName;

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
        if (requestCode == WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_PAGE_REQUESTCODE) {
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
            AppVersionHttpData appVersionHttpData = appVersionHttpResult.getHttpData();
            try {
                float httpVersion = Float.valueOf(appVersionHttpData.getVersion());
                LogHelper.releaseLog(TAG + "setAppVersionHttpResult httpVersion:" + httpVersion + " mVersionCode:" + mVersionCode);
                if (httpVersion > (float) mVersionCode) {
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
                    startActivityForResult(intent, WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_PAGE_REQUESTCODE);
                }
            } else if (PersonalCenterInfo.PERSONAL_CENTER_EXIT.equals(type)) {
                mIsLogin = false;
                AccountHelper.getInstance().logout();
                doLoginResult();
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

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.contact_us));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(PERSONAL_CENTER_CONTACTUS_INDEX, personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.user_agreement));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(PERSONAL_CENTER_USERAGREEMENT_INDEX, personalCenterData);

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
}
