package com.example.synerzip.snapxeats.ui.location;

import com.example.synerzip.snapxeats.ui.preferences.PreferenceInteractor;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationPresenterImpl implements LocationContract.LocationPresenter {
    LocationInteractor interactor;

    public LocationPresenterImpl(LocationInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void takeView(LocationContract.LocationView view) {

    }

    @Override
    public void dropView() {

    }
}
