package com.snapxeats.ui.location;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 30/1/18.
 */

public class LocationRouterImpl implements LocationContract.LocationRouter {
    private Activity activity;
    private Router router;

    @Inject
    public LocationRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(LocationContract.LocationView view) {

    }
}
