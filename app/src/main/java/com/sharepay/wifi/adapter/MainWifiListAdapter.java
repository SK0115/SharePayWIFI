package com.sharepay.wifi.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseAdapter;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.helper.WIFIHelper;
import com.sharepay.wifi.model.info.WIFIInfo;

import java.util.ArrayList;
import java.util.List;

public class MainWifiListAdapter extends BaseAdapter<WIFIInfo> {

    public MainWifiListAdapter(Context context, List<WIFIInfo> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    public void setDatas(List<WIFIInfo> datas) {
        mDatas = datas == null ? new ArrayList<WIFIInfo>() : datas;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.layout_connect_list_item;
    }

    @Override
    protected void convert(BaseHolder holder, WIFIInfo data) {
        TextView textview = holder.getView(R.id.tv_wifi_item_name);
        ImageView ivItem = holder.getView(R.id.iv_wifi_item);
        textview.setText(data.getName());
        String capabilities = data.getCapabilities().trim();
        if (WIFIHelper.isFreeWifi(capabilities)) {
            // 免费wifi根据信号强度显示不同icon
            ivItem.setImageResource(WIFIHelper.getWIFIStateImageBySignalStrength(data.getSignalStrength()));
        } else {
            ivItem.setImageResource(R.drawable.ic_common_wifi_locked);
        }
    }
}
