package com.snapxeats.ui.home.fragment.navpreference;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 9/2/18.
 */


@Singleton
public class NavPrefPresenterImpl implements NavPrefContract.NavPrefPresenter {

    private NavPrefInteractor interactor;
    private NavPrefRouterImpl router;
    private NavPrefContract.NavPrefView navPrefView;

    public NavPrefPresenterImpl(NavPrefInteractor interactor, NavPrefRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(NavPrefContract.NavPrefView view) {
        this.navPrefView = view;
        router.setView(view);
    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {

    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }
}
