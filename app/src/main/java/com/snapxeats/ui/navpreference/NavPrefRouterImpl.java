package com.snapxeats.ui.navpreference;

import com.snapxeats.common.Router;

import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

@Singleton
public class NavPrefRouterImpl implements NavPrefContract.NavPrefRouter {

    private Router router;

    public NavPrefRouterImpl(Router router) {
        this.router = router;
    }
}
