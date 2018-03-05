package com.snapxeats.ui.restaurantInfo;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Singleton
public class RestaurantInfoRouterImpl
        implements RestaurantInfoContract.RestaurantInfoRouter {
    private Activity activity;
    private Router router;

    @Inject
    public RestaurantInfoRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        this.router = router;

    }

    /**
     * Set view to Presenter
     *
     * @param view
     */
    @Override
    public void setView(RestaurantInfoContract.RestaurantInfoView view) {
        this.activity = view.getActivity();
    }
}