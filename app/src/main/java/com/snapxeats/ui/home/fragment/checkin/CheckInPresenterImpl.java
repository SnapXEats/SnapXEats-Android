package com.snapxeats.ui.home.fragment.checkin;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class CheckInPresenterImpl implements CheckInContract.CheckInPresenter {
    private CheckInInteractor interactor;
    private CheckInRouterImpl router;

    public CheckInPresenterImpl(CheckInInteractor interactor, CheckInRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(CheckInContract.CheckInView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
