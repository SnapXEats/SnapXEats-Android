package com.snapxeats.ui.restaurantInfo;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
public class RestaurantInfoContract {
    interface RestaurantInfoView extends BaseView<RestaurantInfoPresenter>, AppContract.SnapXResults {
    }

    interface RestaurantInfoPresenter extends BasePresenter<RestaurantInfoView> {
        void presentScreen(Router.Screen screen);
        void getRestInfo(String restaurantId);
    }

    interface RestaurantInfoRouter {
        void presentScreen(Router.Screen screen);
        void setView(RestaurantInfoView view);
    }
}