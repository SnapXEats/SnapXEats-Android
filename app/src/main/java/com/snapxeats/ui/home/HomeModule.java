package com.snapxeats.ui.home;

import com.snapxeats.common.Router;
import com.snapxeats.dagger.FragmentScoped;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefContract;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.snapxeats.ui.home.fragment.home.HomeFgmtContract;
import com.snapxeats.ui.home.fragment.home.HomeFgmtInteractor;
import com.snapxeats.ui.home.fragment.home.HomeFgmtPresenterImpl;
import com.snapxeats.ui.home.fragment.home.HomeFgmtRouterImpl;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefInteractor;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefPresenterImpl;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefRouterImpl;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Module
public abstract class HomeModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NavPrefFragment navPrefFragment();

    @Provides
    static HomeFgmtContract.HomeFgmtPresenter providePresenter(HomeFgmtInteractor interactor,
                                                               HomeFgmtRouterImpl router) {
        HomeFgmtContract.HomeFgmtPresenter homeFgmtPresenter =
                new HomeFgmtPresenterImpl(interactor, router);
        interactor.setHomeFgmtPresenter(homeFgmtPresenter);
        return homeFgmtPresenter;
    }

    @Provides
    static HomeFgmtRouterImpl providePreferenceRouter(Router router) {
        HomeFgmtRouterImpl preferenceRouter = new HomeFgmtRouterImpl(router);
        return preferenceRouter;
    }

    @Provides
    static NavPrefContract.NavPrefPresenter provideNavPrefPresenter(NavPrefInteractor interactor,
                                                              NavPrefRouterImpl router) {
        NavPrefContract.NavPrefPresenter navPrefPresenter =
                new NavPrefPresenterImpl(interactor, router);
        interactor.setPreferencePresenter(navPrefPresenter);
        return navPrefPresenter;
    }

    @Provides
    static NavPrefRouterImpl provideNavPrefRouter(Router router) {
        NavPrefRouterImpl navPrefRouter = new NavPrefRouterImpl(router);
        return navPrefRouter;
    }
}
