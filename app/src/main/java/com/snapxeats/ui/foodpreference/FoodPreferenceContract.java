package com.snapxeats.ui.foodpreference;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.location.LocationContract;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPreferenceContract {

    interface FoodPreferenceView extends BaseView<FoodPreferencePresenter>, AppContract.SnapXResults {

    }

    interface FoodPreferencePresenter extends BasePresenter<FoodPreferenceView> {
        void presentScreen(Router.Screen screen);

        void getFoodPrefList();
    }

    interface FoodPrefRouter {
        void presentScreen(Router.Screen screen);

        void setView(FoodPreferenceView view);
    }
}
