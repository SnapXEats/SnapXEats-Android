package com.snapxeats.ui.navpreference;

import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

@Singleton
public class NavPrefPresenterImpl implements NavPrefContract.NavPrefPresenter{
    private NavPrefInteractor interactor;
    private NavPrefRouterImpl router;

    public NavPrefPresenterImpl(NavPrefInteractor interactor, NavPrefRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(NavPrefContract.NavPrefView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }
}
