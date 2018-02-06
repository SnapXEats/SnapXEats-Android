package com.snapxeats.ui.foodstack;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
public class FoodStackRouterImpl implements FoodStackContract.FoodStackRouter {
    private Activity mActivity;
    private Router router;

    @Inject
    public FoodStackRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(FoodStackContract.FoodStackView view) {
        this.mActivity = view.getActivity();
    }
}