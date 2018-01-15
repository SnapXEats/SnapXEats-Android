package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;


/**
 * Created by Snehal Tembare on 3/1/18.
 */

public class PreferencePresenterImpl implements PreferenceContract.PreferencePresenter {

    private PreferenceInteractor interactor;
    private Location location;

    @Nullable
    private PreferenceContract.PreferenceView preferenceView;

    private PreferenceContract.PreferenceRouter router;


    public PreferencePresenterImpl(PreferenceInteractor interactor, PreferenceRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void getLocation(PreferenceContract.PreferenceView preferenceView) {
        interactor.getLocation(preferenceView);
    }


    /**
     * Update user location
     *
     * @param placename
     */

    @Override
    public void updatePlace(String placename) {
        preferenceView.updatePlaceName(placename);
    }

    @Override
    public void presentScreen() {
        router.presentScreen();
    }

    /**
     * Set view to Presenter
     * @param view
     */

    @Override
    public void addView(PreferenceContract.PreferenceView view) {
        preferenceView = view;
        router.setView(view);
    }

    @Override
    public void dropView() {
        preferenceView = null;
    }

}
