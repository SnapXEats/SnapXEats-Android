package com.snapxeats.ui.foodpreference;

import com.snapxeats.common.Router;
import com.snapxeats.ui.cuisinepreference.CuisinePrefRouterImpl;
import com.snapxeats.ui.location.LocationRouterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

@Module
public abstract class FoodPrefModule {
    @Provides
    static FoodPreferenceContract.FoodPreferencePresenter providePresenter(FoodPrefInteractor interactor,
                                                                           FoodPrefRouterImpl router) {
        FoodPreferenceContract.FoodPreferencePresenter locationPresenter = new FoodPrefPresenterImpl(interactor, router);
        interactor.setPresenter(locationPresenter);
        return locationPresenter;
    }

    @Provides
    static FoodPrefRouterImpl provideFoodPrefRouter(Router router) {
        FoodPrefRouterImpl foodPrefRouter = new FoodPrefRouterImpl(router);
        return foodPrefRouter;
    }
}
