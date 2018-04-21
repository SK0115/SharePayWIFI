package com.sharepay.wifi.viewModule.personalCenter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.adapter.PersonalCenterAdapter.PersonalCenterItemClickListener;
import com.sharepay.wifi.model.PersonalCenterData;

public class PersonalCenterExitItemView extends RelativeLayout {

    private TextView mExitView;
    private PersonalCenterItemClickListener mClickListener;

    public PersonalCenterExitItemView(Context context) {
        super(context);
        initView(context);
    }

    public PersonalCenterExitItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PersonalCenterExitItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_personal_center_exit_list_item, this, true);
        mExitView = view.findViewById(R.id.tv_personal_center_exit_login);
        mExitView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mClickListener) {
                    mClickListener.click(PersonalCenterData.PERSONAL_CENTER_EXIT);
                }
            }
        });
    }

    public void setClickListener(PersonalCenterItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setData(PersonalCenterData data) {
        if (null != data) {
            if (data.isLogin()) {
                mExitView.setVisibility(View.VISIBLE);
            } else {
                mExitView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
