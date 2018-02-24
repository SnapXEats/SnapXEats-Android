package com.snapxeats.ui.home.fragment.navpreference;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

@Singleton
public class NavPrefRouterImpl implements NavPrefContract.NavPrefRouter {
    private Activity activity;
    private Router router;

    public NavPrefRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(NavPrefContract.NavPrefView view) {
        this.activity = view.getActivity();
    }
}
