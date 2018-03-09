package com.snapxeats.ui.home;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomeRouterImpl implements HomeContract.HomeRouter {
    private Activity activity;
    private Router router;

    @Inject
    public HomeRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(HomeContract.HomeView view) {

    }
}