package com.snapxeats.ui.location;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

import java.util.List;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationPresenterImpl implements LocationContract.LocationPresenter {
    private LocationInteractor interactor;
    private LocationContract.LocationView locationView;
    private LocationRouterImpl router;

    public LocationPresenterImpl(LocationInteractor interactor,
                                 LocationRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(LocationContract.LocationView view) {
        this.locationView = view;
    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != locationView) {
            switch (result) {
                case SUCCESS:
                    locationView.success(value);
                    break;
                case FAILURE:
                    locationView.error(value);
                    break;
                case NONETWORK:
                    locationView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    locationView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void getPlaceDetails(String placeId) {
        interactor.getPlaceDetails(locationView, placeId);
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

}
