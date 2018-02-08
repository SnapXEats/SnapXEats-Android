package com.snapxeats.ui.restaurant;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
@Module
public abstract class RestaurantDetailsModule {
    @Provides
    static RestaurantDetailsContract.RestaurantDetailsPresenter presenter(RestaurantDetailsInteractor interactor,
                                                                          RestaurantDetailsRouterImpl restaurantDetailsRouter) {
        RestaurantDetailsContract.RestaurantDetailsPresenter restaurantDetailsPresenter =
                new RestaurantDetailsPresenterImpl(interactor, restaurantDetailsRouter);
        interactor.setRestaurantDetailsPresenter(restaurantDetailsPresenter);
        return restaurantDetailsPresenter;
    }

    @Provides
    static RestaurantDetailsRouterImpl provideRestaurantDetailsRouter(Router router) {
        RestaurantDetailsRouterImpl restaurantDetailsRouter = new RestaurantDetailsRouterImpl(router);
        return restaurantDetailsRouter;
    }
}
