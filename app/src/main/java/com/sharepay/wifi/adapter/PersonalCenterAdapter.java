package com.sharepay.wifi.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sharepay.wifi.base.BaseAdapter;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.model.info.PersonalCenterInfo;
import com.sharepay.wifi.viewModule.personalCenter.PersonalCenterAccountItemView;
import com.sharepay.wifi.viewModule.personalCenter.PersonalCenterExitItemView;
import com.sharepay.wifi.viewModule.personalCenter.PersonalCenterImgItemView;
import com.sharepay.wifi.viewModule.personalCenter.PersonalCenterTextItemView;

import java.util.ArrayList;
import java.util.List;

public class PersonalCenterAdapter extends BaseAdapter<PersonalCenterInfo> {

    private int mPosition = 0;

    private PersonalCenterItemClickListener mClickListener;

    public PersonalCenterAdapter(Context context, List<PersonalCenterInfo> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    public void setDatas(List<PersonalCenterInfo> datas) {
        mDatas = datas == null ? new ArrayList<PersonalCenterInfo>() : datas;
    }

    public void setClickListener(PersonalCenterItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    protected int getItemLayoutId() {
        return 0;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseHolder baseHolder = null;
        switch (viewType) {
        case TYPE_COMMON_VIEW:
            PersonalCenterInfo data = mDatas.get(mPosition);
            if (PersonalCenterInfo.PERSONAL_CENTER_TEXT.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterTextItemView(mContext));
            } else if (PersonalCenterInfo.PERSONAL_CENTER_IMG.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterImgItemView(mContext));
            } else if (PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterAccountItemView(mContext));
            } else if (PersonalCenterInfo.PERSONAL_CENTER_EXIT.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterExitItemView(mContext));
            }
            break;
        }
        return baseHolder;
    }

    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseHolder holder, PersonalCenterInfo data) {
        if (PersonalCenterInfo.PERSONAL_CENTER_TEXT.equals(data.getType())) {
            ((PersonalCenterTextItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterInfo.PERSONAL_CENTER_IMG.equals(data.getType())) {
            ((PersonalCenterImgItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterInfo.PERSONAL_CENTER_ACCOUNT.equals(data.getType())) {
            ((PersonalCenterAccountItemView) holder.getConvertView()).setClickListener(mClickListener);
            ((PersonalCenterAccountItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterInfo.PERSONAL_CENTER_EXIT.equals(data.getType())) {
            ((PersonalCenterExitItemView) holder.getConvertView()).setClickListener(mClickListener);
            ((PersonalCenterExitItemView) holder.getConvertView()).setData(data);
        }
    }

    /**
     * 点击事件处理
     */
    public interface PersonalCenterItemClickListener {
        void click(String type);
    }
}
