package com.snapxeats.ui.preferences;

import android.location.Location;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter>,AppContract.SnapXResults {

        void updatePlaceName(String placeName);

    }

    interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void setLocation(Location location);

        void getLocation(PreferenceContract.PreferenceView preferenceView);

        void updatePlace(String placename);

        void presentScreen();

    }

    interface PreferenceRouter {
        void presentScreen();
        void setView(PreferenceView view);
    }
}