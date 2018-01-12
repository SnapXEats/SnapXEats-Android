package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.location.Location;
import android.view.View;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter> {

        Activity getActivity();

        void updatePlaceName(String placeName);

    }

    interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void setLocation(Location location);

        void getUserLocation();

        Activity getActivityInstance();

        void updatePlaceName(String placename);

        void presentLocationScreen();

    }

    interface PreferenceRouter {
        void presentScreen();
        void  setView(PreferenceView view);
    }
}