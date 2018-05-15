package com.snapxeats.ui.home.fragment.foodjourney;

import android.app.Activity;

import com.snapxeats.common.Router;

/**
 * Created by Prajakta Patil on 14/5/18.
 */

public class FoodJourneyRouterImpl implements FoodJourneyContract.FoodJourneyRouter {

    private Activity activity;
    private Router router;

    public FoodJourneyRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);

    }

    @Override
    public void setView(FoodJourneyContract.FoodJourneyView view) {
        this.activity = view.getActivity();

    }
}
