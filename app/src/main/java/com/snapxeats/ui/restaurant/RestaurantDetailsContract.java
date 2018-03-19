package com.snapxeats.ui.restaurant;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
public class RestaurantDetailsContract {
    interface RestaurantDetailsView extends BaseView<RestaurantDetailsPresenter>, AppContract.SnapXResults {

    }

    interface RestaurantDetailsPresenter extends BasePresenter<RestaurantDetailsView> {
        void presentScreen(Router.Screen screen);

        void getRestDetails(String restaurantId);

        void getGoogleDirections(LocationGoogleDir locationGoogleDir);
    }

    interface RestaurantDetailsRouter {
        void presentScreen(Router.Screen screen);

        void setView(RestaurantDetailsView view);
    }
}
