package com.sharepay.wifi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.model.PersonalCenterData;

public class PersonalCenterImgItemView extends RelativeLayout {

    private TextView mTitleView;
    private ImageView mImgView;

    public PersonalCenterImgItemView(Context context) {
        super(context);
        initView(context);
    }

    public PersonalCenterImgItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PersonalCenterImgItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_personal_center_img_list_item, this, true);
        mTitleView = view.findViewById(R.id.tv_personal_item_title);
        mImgView = view.findViewById(R.id.iv_personal_item_img);
    }

    public void setData(PersonalCenterData data) {
        if (null != data) {
            mTitleView.setText(data.getTitle());
            mImgView.setBackgroundResource(data.getImg());
        }
    }
}
