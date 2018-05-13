package com.sharepay.wifi.viewModule.personalCenter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.adapter.PersonalCenterAdapter.PersonalCenterItemClickListener;
import com.sharepay.wifi.model.info.PersonalCenterInfo;

public class PersonalCenterTextItemView extends RelativeLayout {

    private TextView mTitleView;
    private TextView mContentView;
    private PersonalCenterItemClickListener mClickListener;

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
        LayoutInflater.from(context).inflate(R.layout.layout_personal_center_text_list_item, this, true);
        mTitleView = findViewById(R.id.tv_personal_textitem_title);
        mContentView = findViewById(R.id.tv_personal_textitem_content);
        mContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mClickListener) {
                    mClickListener.click(PersonalCenterInfo.PERSONAL_CENTER_TEXT);
                }
            }
        });
    }

    public void setClickListener(PersonalCenterItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setData(PersonalCenterInfo data) {
        if (null != data) {
            mTitleView.setText(data.getTitle());
            mContentView.setText(data.getMessage());
        }
    }
}
