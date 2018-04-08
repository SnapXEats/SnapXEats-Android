package com.snapxeats.ui.home.fragment.snapnshare;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class SnapSharePresenterImpl implements SnapShareContract.SnapSharePresenter {
    private SnapShareInteractor interactor;
    private SnapShareRouterImpl router;

    public SnapSharePresenterImpl(SnapShareInteractor interactor, SnapShareRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(SnapShareContract.SnapShareView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
