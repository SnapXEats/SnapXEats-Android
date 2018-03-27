package com.snapxeats.ui.directions;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
@Module
public abstract class DirectionsModule {
    @Provides
    static DirectionsContract.DirectionsPresenter preseneter(DirectionsInteractor interactor,
                                                             DirectionsRouterImpl directionsRouter) {
        DirectionsContract.DirectionsPresenter directionsPresenter
                = new DirectionsPresenterImpl(interactor, directionsRouter);
        interactor.setDirectionsPresenter(directionsPresenter);
        return directionsPresenter;
    }

    @Provides
    static DirectionsRouterImpl provideDirectionsRouter(Router router) {
        DirectionsRouterImpl directionsRouter = new DirectionsRouterImpl(router);
        return directionsRouter;
    }
}