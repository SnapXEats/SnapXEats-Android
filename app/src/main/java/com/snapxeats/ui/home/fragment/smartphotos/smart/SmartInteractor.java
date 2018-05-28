package com.snapxeats.ui.home.fragment.smartphotos.smart;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 27/5/18.
 */

public class SmartInteractor {

    private SmartContract.SmartPresenter smartPresenter;

    @Inject
    public SmartInteractor() {
    }

    public void setSmartPresenter(SmartContract.SmartPresenter smartPresenter) {
        this.smartPresenter = smartPresenter;
    }
}
