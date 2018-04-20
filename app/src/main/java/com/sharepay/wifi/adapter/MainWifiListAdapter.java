package com.sharepay.wifi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseAdapter;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.helper.WIFIHelper;
import com.sharepay.wifi.model.WIFIInfo;

import java.util.ArrayList;
import java.util.List;

public class MainWifiListAdapter extends BaseAdapter<WIFIInfo> {

    private static final String WIFI_AUTH_OPEN = "";
    private static final String WIFI_AUTH_ROAM = "[ESS]";

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
        if (!TextUtils.isEmpty(capabilities) && (capabilities.equals(WIFI_AUTH_OPEN) || capabilities.equals(WIFI_AUTH_ROAM))) {
            ivItem.setImageResource(WIFIHelper.getWIFIStateImageBySignalStrength(data.getSignalStrength()));
        } else {
            ivItem.setImageResource(R.drawable.ic_common_wifi_locked);
        }
    }
}
