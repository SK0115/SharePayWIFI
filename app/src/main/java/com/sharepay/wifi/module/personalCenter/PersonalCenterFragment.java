package com.sharepay.wifi.module.personalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
import com.sharepay.wifi.model.info.PersonalCenterInfo;
import com.sharepay.wifi.model.realm.AccountInfoRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalCenterFragment extends BaseFragment implements PersonalCenterContract.View {

    @BindView(R.id.recyclerview_personal_center)
    RecyclerView mPersonalCenterRecyclerview;

    private PersonalCenterContract.Presenter mPresenter;
    private PersonalCenterAdapter mAdapter;
    private List<PersonalCenterInfo> mPersonalCenterDataList;
    private boolean mIsLogin = false;

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

    /**
     * 处理登陆结果
     */
    private void doLoginResult() {
        AccountInfoRealm accountInfoRealm = AccountHelper.getInstance().getAccountInfo();
        if (null != accountInfoRealm && !TextUtils.isEmpty(accountInfoRealm.getMobile()) && !TextUtils.isEmpty(accountInfoRealm.getId())) {
            mIsLogin = true;
        } else {
            mIsLogin = false;
        }
        resetPersonalCenterDataList(accountInfoRealm);
        if (null == mAdapter) {
            mAdapter = new PersonalCenterAdapter(mActivity, mPersonalCenterDataList, false);
            mAdapter.setClickListener(mClickListener);
            mAdapter.setOnItemClickListener(new OnBaseItemClickListener<PersonalCenterInfo>() {
                @Override
                public void onItemClick(BaseHolder viewHolder, PersonalCenterInfo data, int position) {
                    if (getString(R.string.cost_history).equals(data.getTitle())) {
                        if (mIsLogin) {
                            startActivity(new Intent(mActivity, CostHistoryActivity.class));
                        } else {
                            Toast.makeText(mActivity, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
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

    private void resetPersonalCenterDataList(AccountInfoRealm accountInfoRealm) {
        if (null == mPersonalCenterDataList) {
            mPersonalCenterDataList = new ArrayList<PersonalCenterInfo>();
        }
        mPersonalCenterDataList.clear();
        PersonalCenterInfo personalCenterData = new PersonalCenterInfo();
        if (mIsLogin && null != accountInfoRealm) {
            personalCenterData.setTitle(accountInfoRealm.getMobile());
            personalCenterData.setMessage(accountInfoRealm.getIntegral() + "");
            personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT);
            mPersonalCenterDataList.add(personalCenterData);
        } else {
            personalCenterData.setImg(R.drawable.ic_account_img);
            personalCenterData.setMessage("0");
            personalCenterData.setTitle(getString(R.string.please_login));
            personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT);
            mPersonalCenterDataList.add(personalCenterData);
        }

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.cost_history));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.contact_us));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.user_agreement));
        personalCenterData.setImg(R.drawable.ic_list_next);
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_IMG);
        mPersonalCenterDataList.add(personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setTitle(getString(R.string.version_info));
        personalCenterData.setImg(0);
        personalCenterData.setMessage(getString(R.string.click_update));
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_TEXT);
        mPersonalCenterDataList.add(personalCenterData);

        personalCenterData = new PersonalCenterInfo();
        personalCenterData.setType(PersonalCenterInfo.PERSONAL_CENTER_EXIT);
        personalCenterData.setIsLogin(mIsLogin);
        mPersonalCenterDataList.add(personalCenterData);
    }
}
