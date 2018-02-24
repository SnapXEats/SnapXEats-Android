package com.snapxeats.ui.foodpreference;

import android.app.Activity;
import com.snapxeats.common.Router;
import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPrefRouterImpl implements FoodPreferenceContract.FoodPrefRouter {
    private Activity activity;
    private Router router;

    @Inject
    public FoodPrefRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(FoodPreferenceContract.FoodPreferenceView view) {

    }
}
