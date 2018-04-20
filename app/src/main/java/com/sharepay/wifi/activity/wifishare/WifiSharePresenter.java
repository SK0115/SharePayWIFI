package com.sharepay.wifi.activity.wifishare;

public class WifiSharePresenter implements WifiShareContract.Presenter {

    private WifiShareContract.View mView;

    public WifiSharePresenter(WifiShareContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void onDetach() {
    }
}
