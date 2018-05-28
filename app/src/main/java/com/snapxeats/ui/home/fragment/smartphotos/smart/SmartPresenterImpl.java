package com.snapxeats.ui.home.fragment.smartphotos.smart;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 27/5/18.
 */

public class SmartPresenterImpl implements SmartContract.SmartPresenter {
    private SmartInteractor interactor;
    private SmartRouterImpl router;
    private SmartContract.SmartView smartView;

    public SmartPresenterImpl(SmartInteractor interactor, SmartRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(SmartContract.SmartView view) {
        smartView = view;
    }

    @Override
    public void dropView() {
        smartView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
