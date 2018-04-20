package com.sharepay.wifi.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.sharepay.wifi.base.BaseAdapter;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.model.PersonalCenterData;
import com.sharepay.wifi.view.PersonalCenterAccountItemView;
import com.sharepay.wifi.view.PersonalCenterExitItemView;
import com.sharepay.wifi.view.PersonalCenterImgItemView;
import com.sharepay.wifi.view.PersonalCenterTextItemView;

import java.util.ArrayList;
import java.util.List;

public class PersonalCenterAdapter extends BaseAdapter<PersonalCenterData> {

    private int mPosition = 0;

    private PersonalCenterItemClickListener mClickListener;

    public PersonalCenterAdapter(Context context, List<PersonalCenterData> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    public void setDatas(List<PersonalCenterData> datas) {
        mDatas = datas == null ? new ArrayList<PersonalCenterData>() : datas;
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
            PersonalCenterData data = mDatas.get(mPosition);
            if (PersonalCenterData.PERSONAL_CENTER_TEXT.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterTextItemView(mContext));
            } else if (PersonalCenterData.PERSONAL_CENTER_IMG.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterImgItemView(mContext));
            } else if (PersonalCenterData.PERSONAL_CENTER_ACCOUNT.equals(data.getType())) {
                baseHolder = BaseHolder.create(new PersonalCenterAccountItemView(mContext));
            } else if (PersonalCenterData.PERSONAL_CENTER_EXIT.equals(data.getType())) {
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
    protected void convert(BaseHolder holder, PersonalCenterData data) {
        if (PersonalCenterData.PERSONAL_CENTER_TEXT.equals(data.getType())) {
            ((PersonalCenterTextItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterData.PERSONAL_CENTER_IMG.equals(data.getType())) {
            ((PersonalCenterImgItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterData.PERSONAL_CENTER_ACCOUNT.equals(data.getType())) {
            ((PersonalCenterAccountItemView) holder.getConvertView()).setClickListener(mClickListener);
            ((PersonalCenterAccountItemView) holder.getConvertView()).setData(data);
        } else if (PersonalCenterData.PERSONAL_CENTER_EXIT.equals(data.getType())) {
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
