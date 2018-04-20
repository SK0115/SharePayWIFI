package com.sharepay.wifi.base;

/**
 * Created on 2017/12/20.
 * presenter 再外面使用的应该都是动作
 */

public interface BasePresenter {
    void start();

    /**
     * 销毁动作
     */
    void onDetach();
}
