package com.sharepay.wifi.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.main.MainActivity;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.baseCtrl.MEditText;
import com.sharepay.wifi.baseCtrl.ProgressView;
import com.sharepay.wifi.define.WIFIDefine;
import com.sharepay.wifi.helper.AccountHelper;
import com.sharepay.wifi.helper.BaseTimer;
import com.sharepay.wifi.helper.LogHelper;
import com.sharepay.wifi.model.http.BaseHttpResult;
import com.sharepay.wifi.model.http.LoginAccountHttpData;
import com.sharepay.wifi.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private final String TAG = "LoginFragment ";
    private final int COUNTDOWN_TIME = 60; // 获取验证码倒计时时间

    private int mTiming; // 点击获取验证码
    private BaseTimer mTimer;
    private boolean mIsTiming = false;

    @BindView(R.id.iv_login_colse)
    ImageView mCloseImg;

    @BindView(R.id.edittext_mobile_num)
    MEditText mNumEditText;
    @BindView(R.id.iv_phone_clean)
    ImageView mNumCleanImg;
    @BindView(R.id.image_mobile_num_split_normal)
    ImageView mNumNormalSplitImg;
    @BindView(R.id.image_mobile_num_split_focus)
    ImageView mNumFocusSplitImg;

    @BindView(R.id.edittext_mobile_verifcode)
    MEditText mVerifCodeEditText;
    @BindView(R.id.image_verification_code_split_normal)
    ImageView mVerifNormalSplitImg;
    @BindView(R.id.image_verification_code_split_focus)
    ImageView mVerifFocusSplitImg;

    @BindView(R.id.text_verificode_error)
    TextView mVerifCodeError;

    @BindView(R.id.text_getverifi_code)
    TextView mGetVeriCodeText;
    @BindView(R.id.text_login_view)
    TextView mLoginText;
    @BindView(R.id.text_jumplogin_view)
    TextView mJumpLoginText;

    @BindView(R.id.layout_loading)
    RelativeLayout mLoadingLayout;
    @BindView(R.id.pb_loading)
    ProgressView mLoadingProgressBar;

    private String mActivityFormText;

    @OnClick({ R.id.iv_login_colse, R.id.iv_phone_clean, R.id.text_getverifi_code, R.id.text_login_view, R.id.text_jumplogin_view, R.id.layout_loading })

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.iv_login_colse:
            if (null != mActivity) {
                mActivity.finish();
            }
            break;
        case R.id.iv_phone_clean:
            String num = mNumEditText.getText().toString();
            if (!TextUtils.isEmpty(num)) {
                mNumEditText.setText("");
            }
            break;
        case R.id.text_getverifi_code:
            if (mIsTiming) {
                return;
            }
            mVerifCodeEditText.setText("");
            mVerifFocusSplitImg.setImageResource(R.color.color_login_bg);
            mVerifNormalSplitImg.setVisibility(mVerifCodeEditText.hasFocus() ? View.INVISIBLE : View.VISIBLE);
            mVerifFocusSplitImg.setVisibility(mVerifCodeEditText.hasFocus() ? View.VISIBLE : View.INVISIBLE);
            mVerifCodeError.setVisibility(View.INVISIBLE);
            String mobile = mNumEditText.getText().toString();
            LogHelper.releaseLog(TAG + "getVerifiCode mobile:" + mobile);
            if (TextUtils.isEmpty(mobile)) {
                ToastUtils.showShort(R.string.phonenum_is_null);
            } else if (mobile.length() < 11) {
                ToastUtils.showShort(R.string.phonenum_less_eleven);
            } else if (mobile.length() > 11) {
                ToastUtils.showShort(R.string.phonenum_more_eleven);
            } else {
                mPresenter.requestVerificationCode(mobile);
                startCountdown();
            }
            break;
        case R.id.text_login_view:
            String mobileNum = mNumEditText.getText().toString();
            String code = mVerifCodeEditText.getText().toString();
            LogHelper.releaseLog(TAG + "login mobileNum:" + mobileNum + " verifiCode:" + code);
            if (TextUtils.isEmpty(mobileNum)) {
                ToastUtils.showShort(R.string.phonenum_is_null);
                return;
            } else if (mobileNum.length() < 11) {
                ToastUtils.showShort(R.string.phonenum_less_eleven);
                return;
            } else if (mobileNum.length() > 11) {
                ToastUtils.showShort(R.string.phonenum_more_eleven);
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort(R.string.verificode_is_null);
                return;
            } else if (code.length() < 6) {
                ToastUtils.showShort(R.string.verificode_less_six);
                return;
            } else if (code.length() > 6) {
                ToastUtils.showShort(R.string.verificode_more_six);
                return;
            }
            mLoadingLayout.setVisibility(View.VISIBLE);
            mLoadingProgressBar.setImageResource(R.drawable.ic_list_loading);
            mLoadingProgressBar.startRotateAnimation();
            mPresenter.requestUserlogin(mobileNum, code);
            break;
        case R.id.text_jumplogin_view:
            if (WIFIDefine.JUMP_ACTIVITY.PERSONAL_CENTER.equals(mActivityFormText)) {
                mActivity.setResult(WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_PAGE_REQUESTCODE);
            } else {
                startActivity(new Intent(mActivity, MainActivity.class));
            }
            mActivity.finish();
            break;
        default:
            break;
        }
    }

    private LoginActivity mActivity;
    private LoginContract.Presenter mPresenter;

    public static LoginFragment getInstance() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        Intent intent = mActivity.getIntent();
        mActivityFormText = intent.getStringExtra(WIFIDefine.ACTIVITY_JUMP_FROM);
        setHintTextSize(mNumEditText, getResources().getString(R.string.mobile_num), 17);
        setHintTextSize(mVerifCodeEditText, getResources().getString(R.string.verification_code), 17);

        mNumEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (null != mNumEditText && null != mNumNormalSplitImg && null != mNumFocusSplitImg) {
                    if (hasFocus) {
                        mNumEditText.setCursorVisible(true);
                        mNumNormalSplitImg.setVisibility(View.INVISIBLE);
                        mNumFocusSplitImg.setVisibility(View.VISIBLE);
                    } else {
                        String num = mNumEditText.getText().toString();
                        if (TextUtils.isEmpty(num)) {
                            mNumEditText.setCursorVisible(false);
                            mNumNormalSplitImg.setVisibility(View.VISIBLE);
                            mNumFocusSplitImg.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
        mVerifCodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (null != mVerifCodeEditText && null != mVerifNormalSplitImg && null != mVerifFocusSplitImg) {
                    if (hasFocus) {
                        mVerifCodeEditText.setCursorVisible(true);
                        mVerifNormalSplitImg.setVisibility(View.INVISIBLE);
                        mVerifFocusSplitImg.setImageResource(R.color.color_login_bg);
                        mVerifFocusSplitImg.setVisibility(View.VISIBLE);
                    } else {
                        String code = mVerifCodeEditText.getText().toString();
                        if (TextUtils.isEmpty(code)) {
                            mVerifCodeEditText.setCursorVisible(false);
                            mVerifNormalSplitImg.setVisibility(View.VISIBLE);
                            mVerifFocusSplitImg.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mLoadingProgressBar) {
            mLoadingProgressBar.stopRotateAnimation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountdown(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoginAccountHttpResult(BaseHttpResult<LoginAccountHttpData> loginAccountHttpData) {
        if (null != loginAccountHttpData && WIFIDefine.HttpResultState.SUCCESS.equals(loginAccountHttpData.getStatus())) {
            AccountHelper.getInstance().addAccountInfoToRealm(loginAccountHttpData);
            doLoginResult();
        } else {
            mVerifFocusSplitImg.setImageResource(R.color.color_verificode_error);
            mVerifCodeError.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.INVISIBLE);
            mLoadingProgressBar.stopRotateAnimation();
        }
    }

    private void doLoginResult() {
        ToastUtils.showShort(R.string.login_success);
        if (WIFIDefine.JUMP_ACTIVITY.PERSONAL_CENTER.equals(mActivityFormText)) {
            mActivity.setResult(WIFIDefine.JUMP_PAGE_REQUESTCODE.JUMP_PAGE_REQUESTCODE);
        } else {
            startActivity(new Intent(mActivity, MainActivity.class));
        }
        mActivity.finish();
    }

    private void setHintTextSize(MEditText editText, String hintText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    /**
     * 开始获取验证码倒计时
     */
    private void startCountdown() {
        stopCountdown(true);
        mIsTiming = true;
        mTimer = new BaseTimer();
        mTimer.startInterval(1000, new BaseTimer.TimerCallBack() {
            @Override
            public void callback() {
                mTiming--;
                if (mTiming == 0) {
                    stopCountdown(true);
                } else {
                    String time = mTiming + " 秒";
                    if (mTiming < 10) {
                        time = "   " + time;
                    }
                    mGetVeriCodeText.setText(time);
                    mGetVeriCodeText.setTextColor(mActivity.getResources().getColor(R.color.white));
                    mGetVeriCodeText.setBackgroundResource(R.drawable.get_verificode_timing_bg);
                }
            }
        });
    }

    /**
     * 停止获取验证码倒计时
     * 
     * @param isResetGetVeriCodeView
     *            是否重置获取验证码view
     */
    private void stopCountdown(boolean isResetGetVeriCodeView) {
        if (null != mTimer) {
            if (mTimer.isRunning()) {
                mTimer.killTimer();
            }
            mTimer = null;
        }
        mTiming = COUNTDOWN_TIME;
        mIsTiming = false;
        if (isResetGetVeriCodeView) {
            mGetVeriCodeText.setText(getString(R.string.get_verification_code));
            mGetVeriCodeText.setTextColor(getResources().getColor(R.color.color_mobile_login));
            mGetVeriCodeText.setBackgroundResource(R.drawable.get_verificode_selector_bg);
        }
    }
}
