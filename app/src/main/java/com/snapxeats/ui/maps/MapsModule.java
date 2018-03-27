package com.snapxeats.ui.maps;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
@Module
public abstract class MapsModule {
    @Provides
    static MapsContract.MapsPresenter preseneter(MapsInteractor interactor,
                                                             MapsRouterImpl mapsRouter) {
        MapsContract.MapsPresenter mapsPresenter
                = new MapsPresenterImpl(interactor, mapsRouter);
        interactor.setMapsPresenter(mapsPresenter);
        return mapsPresenter;
    }

    @Provides
    static MapsRouterImpl provideMapsRouter(Router router) {
        MapsRouterImpl mapsRouter = new MapsRouterImpl(router);
        return mapsRouter;
    }
}
