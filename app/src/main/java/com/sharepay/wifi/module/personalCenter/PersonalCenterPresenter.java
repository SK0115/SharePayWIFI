package com.sharepay.wifi.module.personalCenter;

public class PersonalCenterPresenter implements PersonalCenterContract.Presenter {

    private PersonalCenterContract.View mView;

    public PersonalCenterPresenter(PersonalCenterContract.View view) {
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
