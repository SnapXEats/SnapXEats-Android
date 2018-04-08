package com.snapxeats.ui.home.fragment.foodjourney;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Snehal Tembare on 8/4/18.
 */

public class FoodJourneyContract {

    public interface FoodJourneyView extends BaseView<FoodJourneyPresenter>,AppContract.SnapXResults{

    }

    public interface FoodJourneyPresenter extends BasePresenter<FoodJourneyView> {
    }

    public interface FoodJourneyRouter{

    }
}
