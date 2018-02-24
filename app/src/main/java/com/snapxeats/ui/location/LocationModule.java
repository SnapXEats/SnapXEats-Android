package com.snapxeats.ui.location;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

@Module
public abstract class LocationModule {
    @Provides
    static LocationContract.LocationPresenter providePresenter(LocationInteractor interactor,
                                                               LocationRouterImpl router) {
        LocationContract.LocationPresenter locationPresenter = new LocationPresenterImpl(interactor, router);
        interactor.setLocationPresenter(locationPresenter);
        return locationPresenter;
    }
}
