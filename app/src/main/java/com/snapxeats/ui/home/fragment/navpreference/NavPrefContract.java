package com.snapxeats.ui.home.fragment.navpreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

public class NavPrefContract {

    public interface NavPrefView extends BaseView<NavPrefPresenter>, AppContract.SnapXResults {

    }

    public interface NavPrefPresenter extends BasePresenter<NavPrefView> {
        void presentScreen(Router.Screen screen);

        void savePreferences(UserPreference userPreference);

        void updatePreferences(UserPreference userPreference);

        void saveLocalData(UserPreference userPreference);

        void saveUserData();
    }

    public interface NavPrefRouter {
        void presentScreen(Router.Screen screen);

        void setView(NavPrefContract.NavPrefView view);
    }
}
