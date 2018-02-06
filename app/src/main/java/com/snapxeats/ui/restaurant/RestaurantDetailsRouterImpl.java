package com.snapxeats.ui.restaurant;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 5/2/18.
 */

@Singleton
public class RestaurantDetailsRouterImpl implements RestaurantDetailsContract.RestaurantDetailsRouter {

    private Activity activity;
    private Router router;

    @Inject
    public RestaurantDetailsRouterImpl(Router router) {
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
    public void setView(RestaurantDetailsContract.RestaurantDetailsView view) {
        this.activity = view.getActivity();
    }
}
