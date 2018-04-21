package com.sharepay.wifi.module.guide;

public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View mView;

    public GuidePresenter(GuideContract.View view) {
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
