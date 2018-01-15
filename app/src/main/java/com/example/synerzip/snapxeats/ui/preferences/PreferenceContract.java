package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Location;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;
import com.example.synerzip.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter> {

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