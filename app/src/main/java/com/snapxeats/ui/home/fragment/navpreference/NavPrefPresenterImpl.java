package com.snapxeats.ui.home.fragment.navpreference;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.preference.UserPreference;
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
        interactor.setContext(view);
    }

    @Override
    public void dropView() {
        navPrefView = null;
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (navPrefView != null) {
            switch (result) {
                case SUCCESS:
                    navPrefView.success(value);
                    break;
                case FAILURE:
                    navPrefView.error(value);
                    break;
                case NONETWORK:
                    navPrefView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    navPrefView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void savePreferences(UserPreference userPreference) {
        interactor.applyPreferences(userPreference);
    }

    @Override
    public void updatePreferences(UserPreference userPreference) {
        interactor.updatePreferences(userPreference);
    }

    @Override
    public void saveLocalData(UserPreference userPreference) {
        interactor.saveDataInLocalDb(userPreference);
    }

    @Override
    public void saveUserData() {
        interactor.saveUserData();
    }
}
