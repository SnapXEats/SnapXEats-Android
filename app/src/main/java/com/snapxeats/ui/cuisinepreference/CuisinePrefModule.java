package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.common.Router;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 13/2/18.
 */


@Module
public abstract class CuisinePrefModule {
    @Provides
    static CuisinePrefContract.CuisinePrefPresenter providePresenter(CuisinePrefInteractor interactor,
                                                                     CuisinePrefRouterImpl router) {
        CuisinePrefContract.CuisinePrefPresenter prefPresenter = new CuisinePrefPresenterImpl(interactor, router);
        interactor.setPresenter(prefPresenter);
        return prefPresenter;
    }

    @Provides
    static CuisinePrefRouterImpl provideCuisinePrefRouter(Router router) {
        CuisinePrefRouterImpl cuisinePrefRouter = new CuisinePrefRouterImpl(router);
        return cuisinePrefRouter;
    }
}
