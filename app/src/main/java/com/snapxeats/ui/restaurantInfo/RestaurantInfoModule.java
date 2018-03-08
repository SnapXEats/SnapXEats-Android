package com.snapxeats.ui.restaurantInfo;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Module
public abstract class RestaurantInfoModule {
    @Provides
    static RestaurantInfoContract.RestaurantInfoPresenter presenter(RestaurantInfoInteractor interactor,
                                                                    RestaurantInfoRouterImpl restaurantInfoRouter) {
        RestaurantInfoContract.RestaurantInfoPresenter restaurantInfoPresenter =
                new RestaurantInfoPresenterImpl(interactor, restaurantInfoRouter);
        interactor.setRestaurantInfoPresenter(restaurantInfoPresenter);
        return restaurantInfoPresenter;
    }

    @Provides
    static RestaurantInfoRouterImpl provideRestaurantInfoRouter(Router router) {
        return new RestaurantInfoRouterImpl(router);
    }
}