package com.snapxeats.ui.location;

import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationPresenterImpl implements LocationContract.LocationPresenter {
    LocationInteractor interactor;

    public LocationPresenterImpl(LocationInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void addView(LocationContract.LocationView view) {

    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result) {

    }
}
