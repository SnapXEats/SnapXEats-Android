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
    public void getUserLocation() {
        interactor.getUserLocation();
    }

    @Override
    public void showPermissionDialog() {
        if (ActivityCompat.checkSelfPermission(preferenceView.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(preferenceView.getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(preferenceView.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PreferenceActivity.PreferenceConstant.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public Activity getActivityInstance() {
        return preferenceView.getActivity();
    }

    /**
     * Update user location
     *
     * @param placename
     */

    @Override
    public void updatePlaceName(String placename) {
        preferenceView.updatePlaceName(placename);
    }

    @Override
    public void showProgressDialog() {
        preferenceView.showProgressDialog();
    }

    @Override
    public void dismissProgressDialog() {
        preferenceView.dismissProgressDialog();

    }

    @Override
    public void showDenyDialog() {
        preferenceView.showDenyDialog();
    }

    @Override
    public void presentLocationScreen() {
        router.presentLocationScreen();
    }

    @Override
    public void showNetworkErrorDialog() {
        preferenceView.showNetworkErrorDialog();
    }

    /**
     * Set view to Presenter
     * @param view
     */

    @Override
    public void takeView(PreferenceContract.PreferenceView view) {
        preferenceView = view;
        router.setView(view);
    }

    @Override
    public void dropView() {
        preferenceView = null;
    }

}
