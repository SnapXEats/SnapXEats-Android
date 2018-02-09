package com.snapxeats.ui.restaurant;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
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
    public void response(SnapXResult result,Object value) {
        switch (result) {
            case SUCCESS:
                if (mRestaurantDetailsView != null) {
                    mRestaurantDetailsView.success(value);
                }
                break;
            case FAILURE:
                if (mRestaurantDetailsView != null) {
                    mRestaurantDetailsView.error();
                }
                break;
            case NONETWORK:
                if (mRestaurantDetailsView != null) {
                    mRestaurantDetailsView.noNetwork(value);
                }
                break;
            case NETWORKERROR:
                if (mRestaurantDetailsView != null) {
                    mRestaurantDetailsView.networkError();
                }
                break;
        }
    }
}
