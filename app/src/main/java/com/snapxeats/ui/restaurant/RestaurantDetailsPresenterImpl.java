package com.snapxeats.ui.restaurant;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
@Singleton
public class RestaurantDetailsPresenterImpl implements RestaurantDetailsContract.RestaurantDetailsPresenter {
    private RestaurantDetailsInteractor mRestaurantDetailsInteractor;

    @Nullable
    private RestaurantDetailsContract.RestaurantDetailsView mRestaurantDetailsView;

    private RestaurantDetailsRouterImpl mDetailsRouter;

    RestaurantDetailsPresenterImpl(RestaurantDetailsInteractor mInteractor, RestaurantDetailsRouterImpl router) {
        this.mRestaurantDetailsInteractor = mInteractor;
        this.mDetailsRouter = router;
    }

    @Override
    public void addView(RestaurantDetailsContract.RestaurantDetailsView view) {
        mRestaurantDetailsView = view;
        mDetailsRouter.setView(view);
        mRestaurantDetailsInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mRestaurantDetailsView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mDetailsRouter.presentScreen(screen);

    }

    @Override
    public void getRestDetails(String restaurantId) {
        mRestaurantDetailsInteractor.getRestDetails(restaurantId);
    }

    @Override
    public void getGoogleDirections(LocationGoogleDir locationGoogleDir) {
        mRestaurantDetailsInteractor.getGoogleDirections(locationGoogleDir);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mRestaurantDetailsView) {
            switch (result) {
                case SUCCESS:
                    mRestaurantDetailsView.success(value);
                    break;
                case FAILURE:
                    mRestaurantDetailsView.error(value);
                    break;
                case NONETWORK:
                    mRestaurantDetailsView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mRestaurantDetailsView.networkError(value);
                    break;
            }
        }

    }
}
