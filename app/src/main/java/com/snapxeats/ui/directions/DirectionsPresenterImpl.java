package com.snapxeats.ui.directions;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
@Singleton
public class DirectionsPresenterImpl implements DirectionsContract.DirectionsPresenter {
    private DirectionsRouterImpl mDirectionsRouter;

    private DirectionsInteractor mDirectionsInteractor;

    @Nullable
    private DirectionsContract.DirectionsView mDirectionsView;

    public DirectionsPresenterImpl(DirectionsInteractor directionsInteractor,
                                   DirectionsRouterImpl directionsRouter) {
        this.mDirectionsInteractor = directionsInteractor;
        this.mDirectionsRouter = directionsRouter;
    }

    @Override
    public void addView(DirectionsContract.DirectionsView view) {
        this.mDirectionsView = view;
        mDirectionsRouter.setView(view);
        mDirectionsInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mDirectionsView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mDirectionsRouter.presentScreen(screen);
    }

    @Override
    public void checkIn(CheckInRequest checkInRequest) {
        mDirectionsInteractor.checkIn(checkInRequest);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mDirectionsView) {
            switch (result) {
                case SUCCESS:
                    mDirectionsView.success(value);
                    break;
                case FAILURE:
                    mDirectionsView.error(value);
                    break;
                case NONETWORK:
                    mDirectionsView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mDirectionsView.networkError(value);
                    break;
            }
        }
    }
}