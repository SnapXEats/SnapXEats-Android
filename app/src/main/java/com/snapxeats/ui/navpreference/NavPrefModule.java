package com.snapxeats.ui.navpreference;

import com.snapxeats.common.Router;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 9/2/18.
 */


@Module
public abstract class NavPrefModule {
    @Provides
    static NavPrefContract.NavPrefPresenter providePresenter(NavPrefInteractor interactor,
                                                             NavPrefRouterImpl router) {
        NavPrefContract.NavPrefPresenter navPrefPresenter = new NavPrefPresenterImpl(interactor, router);

        interactor.setPreferencePresenter(navPrefPresenter);
        return navPrefPresenter;
    }

    @Provides
    static NavPrefRouterImpl provideNavPrefRouter(Router router) {
        NavPrefRouterImpl navPrefRouter = new NavPrefRouterImpl(router);
        return navPrefRouter;
    }
}
