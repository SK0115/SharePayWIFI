package com.sharepay.wifi.viewModule.personalCenter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.adapter.PersonalCenterAdapter.PersonalCenterItemClickListener;
import com.sharepay.wifi.model.PersonalCenterData;

public class PersonalCenterAccountItemView extends RelativeLayout {

    private ImageView mPhotoView;
    private TextView mNumView;
    private TextView mPointView;

    private PersonalCenterItemClickListener mClickListener;

    public PersonalCenterAccountItemView(Context context) {
        super(context);
        initView(context);
    }

    public PersonalCenterAccountItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PersonalCenterAccountItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_personal_center_account_list_item, this, true);
        mPhotoView = view.findViewById(R.id.iv_personal_center_account_img);
        mNumView = view.findViewById(R.id.tv_personal_center_account_num);
        mPointView = view.findViewById(R.id.tv_personal_center_point);

        mPhotoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mClickListener) {
                    mClickListener.click(PersonalCenterData.PERSONAL_CENTER_ACCOUNT);
                }
            }
        });
    }

    public void setClickListener(PersonalCenterItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setData(PersonalCenterData data) {
        if (null != data) {
            mPhotoView.setBackgroundResource(data.getImg());
            mNumView.setText(data.getTitle());
            mPointView.setText(data.getMessage());
        }
    }
}
