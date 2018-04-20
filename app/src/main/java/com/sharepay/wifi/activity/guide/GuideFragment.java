package com.sharepay.wifi.activity.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rd.Orientation;
import com.rd.PageIndicatorView;
import com.sharepay.wifi.R;
import com.sharepay.wifi.SPApplication;
import com.sharepay.wifi.activity.login.LoginActivity;
import com.sharepay.wifi.activity.main.MainActivity;
import com.sharepay.wifi.base.BaseFragment;
import com.sharepay.wifi.helper.RealmHelper;
import com.sharepay.wifi.model.AccountInfoRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideFragment extends BaseFragment implements GuideContract.View {

    private static final String TAG = "GuideFragment ";

    @BindView(R.id.guide_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.guide_pageindicatorview)
    PageIndicatorView pageIndicatorView;
    private boolean isLeft = false; // 判断是否滑向左边
    private boolean isRight = false; // 判断是否滑向右边
    private boolean isScrolling = false; // 判断是否滑动中
    private int lastValue = -1; // 最后的位置

    @OnClick({ R.id.guide_viewpager, R.id.guide_pageindicatorview })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.guide_viewpager:
            break;
        case R.id.guide_pageindicatorview:
            break;
        default:
            break;
        }
    }

    private GuideActivity mActivity;
    private GuideContract.Presenter mPresenter;

    public static GuideFragment getInstance() {
        GuideFragment guideFragment = new GuideFragment();
        Bundle bundle = new Bundle();
        guideFragment.setArguments(bundle);
        return guideFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView() {
        GuideAdapter adapter = new GuideAdapter();
        adapter.setData(createPageList());
        mViewPager.setAdapter(adapter);

        pageIndicatorView.setViewPager(mViewPager);
        pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // pos :当前页面，及你点击滑动的页面
                // arg1:当前页面偏移的百分比
                // arg2:当前页面偏移的像素位置

                if (isScrolling) {
                    if (lastValue > positionOffsetPixels) {
                        // 递减，向右侧滑动
                        isRight = true;
                        isLeft = false;
                    } else if (lastValue < positionOffsetPixels) {
                        // 递减，向右侧滑动
                        isRight = false;
                        isLeft = true;
                    } else if (lastValue == positionOffsetPixels) {
                        isRight = isLeft = false;
                    }
                }

                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    pageIndicatorView.setVisibility(View.GONE);
                } else
                    pageIndicatorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 有三种状态（0，1，2）。
                // state == 0 表示什么都没做。
                // state == 1 表示正在滑动
                // state == 2 表示滑动完毕了
                if (state == 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }

                if (state == 2) {
                    isRight = isLeft = false;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mActivity = (GuideActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @NonNull
    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        View view_one = LayoutInflater.from(mActivity).inflate(R.layout.start_one, null, false);
        View view_two = LayoutInflater.from(mActivity).inflate(R.layout.start_one, null, false);
        View view_three = LayoutInflater.from(mActivity).inflate(R.layout.start_one, null, false);
        TextView tvNow = view_three.findViewById(R.id.tv_now);
        tvNow.setVisibility(View.VISIBLE);
        tvNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
                if (RealmHelper.getInstance().isRealmObjectExist(accountInfoRealm, "loginKey", AccountInfoRealm.LOGIN_KEY)) {
                    startActivity(new Intent(mActivity, MainActivity.class));
                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
                SPApplication.clearLastActivity();
                mActivity.finish();
            }
        });
        pageList.add(view_one);
        pageList.add(view_two);
        pageList.add(view_three);
        return pageList;
    }

    class GuideAdapter extends PagerAdapter {

        private List<View> viewList;

        GuideAdapter() {
            this.viewList = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = viewList.get(position);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        void setData(@Nullable List<View> list) {
            this.viewList.clear();
            if (list != null && !list.isEmpty()) {
                this.viewList.addAll(list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        List<View> getData() {
            if (viewList == null) {
                viewList = new ArrayList<>();
            }
            return viewList;
        }
    }
}
