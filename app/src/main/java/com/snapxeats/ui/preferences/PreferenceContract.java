package com.snapxeats.ui.preferences;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter>, AppContract.SnapXResults {

    }

    public interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void presentScreen(Router.Screen screen);

        void getCuisineList(PreferenceContract.PreferenceView preferenceView,
                            LocationCuisine locationCuisine);

        void getUserData(PreferenceContract.PreferenceView preferenceView,SnapXUserRequest snapXUserRequest);

    }

    interface PreferenceRouter {
        void presentScreen(Router.Screen screen);

        void setView(PreferenceView view);
    }
}