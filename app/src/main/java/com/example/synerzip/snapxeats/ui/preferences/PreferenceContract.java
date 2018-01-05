package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.location.Location;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferenceContract {

    interface PreferenceView extends BaseView<PreferencePresenter> {

        Activity getActivity();
        void updatePlaceName(String place);
        void showProgressDialog();
        void dismissProgressDialog();
    }

    interface PreferencePresenter extends BasePresenter<PreferenceView> {

        void setLocation(Location location);
        void getUserLocation();
        void showPermissionDialog();
        Activity getActivityInstance();
        void updatePlaceName(String placename);
        void showProgressDialog();
        void dismissProgressDialog();

    }
}