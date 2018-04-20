package com.sharepay.wifi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.model.PersonalCenterData;

public class PersonalCenterTextItemView extends RelativeLayout {

    private TextView mTitleView;
    private TextView mContentView;

    public PersonalCenterTextItemView(Context context) {
        super(context);
        initView(context);
    }

    public PersonalCenterTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PersonalCenterTextItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_personal_center_text_list_item, this, true);
        mTitleView = view.findViewById(R.id.tv_personal_textitem_title);
        mContentView = view.findViewById(R.id.tv_personal_textitem_content);
    }

    public void setData(PersonalCenterData data) {
        if (null != data) {
            mTitleView.setText(data.getTitle());
            mContentView.setText(data.getMessage());
        }
    }
}
