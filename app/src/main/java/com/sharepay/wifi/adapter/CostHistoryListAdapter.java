package com.sharepay.wifi.adapter;

import android.content.Context;
import android.widget.TextView;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseAdapter;
import com.sharepay.wifi.base.BaseHolder;
import com.sharepay.wifi.model.CostHistoryInfo;

import java.util.List;

public class CostHistoryListAdapter extends BaseAdapter<CostHistoryInfo> {

    public CostHistoryListAdapter(Context context, List<CostHistoryInfo> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.layout_cost_history_list_item;
    }

    @Override
    protected void convert(BaseHolder holder, CostHistoryInfo data) {
        TextView contentTextview = holder.getView(R.id.tv_cost_history_content);
        contentTextview.setText(data.getContent());
        TextView timeTextView = holder.getView(R.id.tv_cost_history_time);
        timeTextView.setText(data.getTime());
        TextView intergrationTextView = holder.getView(R.id.tv_cost_history_intergration);
        intergrationTextView.setText(data.getIntegration());
    }
}
