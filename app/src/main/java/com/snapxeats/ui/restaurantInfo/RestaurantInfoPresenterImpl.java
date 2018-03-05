package com.snapxeats.ui.restaurantInfo;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
public class RestaurantInfoPresenterImpl
        implements RestaurantInfoContract.RestaurantInfoPresenter {
    private RestaurantInfoInteractor mRestaurantInfoInteractor;

    @Nullable
    private RestaurantInfoContract.RestaurantInfoView mRestaurantInfoView;

    private RestaurantInfoRouterImpl mInfoRouter;

    RestaurantInfoPresenterImpl(RestaurantInfoInteractor mInteractor, RestaurantInfoRouterImpl router) {
        this.mRestaurantInfoInteractor = mInteractor;
        this.mInfoRouter = router;
    }

    @Override
    public void addView(RestaurantInfoContract.RestaurantInfoView view) {
        mRestaurantInfoView = view;
        mInfoRouter.setView(view);
        mRestaurantInfoInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mRestaurantInfoView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mInfoRouter.presentScreen(screen);
    }

    @Override
    public void getRestInfo(String restaurantId) {
        mRestaurantInfoInteractor.getRestInfo(restaurantId);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mRestaurantInfoView) {
            switch (result) {
                case SUCCESS:
                    mRestaurantInfoView.success(value);
                    break;
                case FAILURE:
                    mRestaurantInfoView.error(value);
                    break;
                case NONETWORK:
                    mRestaurantInfoView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mRestaurantInfoView.networkError(value);
                    break;
            }
        }
    }
}