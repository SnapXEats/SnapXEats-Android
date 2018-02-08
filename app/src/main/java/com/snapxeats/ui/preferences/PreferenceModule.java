package com.snapxeats.ui.preferences;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Module
public abstract class PreferenceModule {

    @Provides
    static PreferenceContract.PreferencePresenter providePresenter(PreferenceInteractor interactor,
                                                                   PreferenceRouterImpl router) {
        PreferenceContract.PreferencePresenter preferencePresenter =
                new PreferencePresenterImpl(interactor, router);
        interactor.setPreferencePresenter(preferencePresenter);
        return preferencePresenter;
    }

    @Provides
    static PreferenceRouterImpl providePreferenceRouter(Router router) {
        PreferenceRouterImpl preferenceRouter = new PreferenceRouterImpl(router);
        return preferenceRouter;
    }

}
