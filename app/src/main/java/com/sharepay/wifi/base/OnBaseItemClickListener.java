package com.sharepay.wifi.base;


/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnBaseItemClickListener<T> {
    void onItemClick(BaseHolder viewHolder, T data, int position);
}
