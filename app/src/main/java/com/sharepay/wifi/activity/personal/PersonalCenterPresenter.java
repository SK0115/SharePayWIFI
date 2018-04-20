package com.sharepay.wifi.activity.personal;

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
