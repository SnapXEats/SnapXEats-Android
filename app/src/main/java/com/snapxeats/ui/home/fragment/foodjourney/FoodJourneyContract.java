package com.snapxeats.ui.home.fragment.foodjourney;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 14/5/18.
 */

public class FoodJourneyContract {

    interface FoodJourneyView extends BaseView<FoodJourneyContract.FoodJourneyPresenter>, AppContract.SnapXResults {

    }

    public interface FoodJourneyPresenter extends BasePresenter<FoodJourneyContract.FoodJourneyView> {
        void presentScreen(Router.Screen screen);

        void getFoodJourney();
    }

    public interface FoodJourneyRouter {
        void presentScreen(Router.Screen screen);

        void setView(FoodJourneyContract.FoodJourneyView view);
    }
}
