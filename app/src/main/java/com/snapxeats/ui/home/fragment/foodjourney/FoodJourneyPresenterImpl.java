package com.snapxeats.ui.home.fragment.foodjourney;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Prajakta Patil on 14/5/18.
 */

public class FoodJourneyPresenterImpl implements FoodJourneyContract.FoodJourneyPresenter {
    private FoodJourneyInteractor interactor;
    private FoodJourneyRouterImpl router;
    private FoodJourneyContract.FoodJourneyView foodJourneyView;


    public FoodJourneyPresenterImpl(FoodJourneyInteractor interactor, FoodJourneyRouterImpl router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void addView(FoodJourneyContract.FoodJourneyView view) {
        this.foodJourneyView = view;
        router.setView(view);
        interactor.setContext(view);
    }

    @Override
    public void dropView() {

    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != foodJourneyView) {
            switch (result) {
                case SUCCESS:
                    foodJourneyView.success(value);
                    break;
                case FAILURE:
                    foodJourneyView.error(value);
                    break;
                case NONETWORK:
                    foodJourneyView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    foodJourneyView.networkError(value);
                    break;
            }
        }
    }

    @Override
    public void getFoodJourney() {
        interactor.getFoodJourney();
    }

    @Override
    public void presentScreen(Router.Screen screen) {

    }
}