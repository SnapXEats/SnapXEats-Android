package com.snapxeats.ui.preferences;

import android.location.Location;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.dagger.AppContract;

import java.util.ArrayList;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter>, AppContract.SnapXResults {

        void updatePlaceName(String placeName, Location location);
    }

    interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void getLocation(PreferenceContract.PreferenceView preferenceView);

        void updatePlace(String placename, Location location);

        void presentScreen(Router.Screen screen);

        void getCuisineList();

    }

    interface PreferenceRouter {
        void presentScreen(Router.Screen screen);

        void setView(PreferenceView view);
    }
}