package com.snapxeats.ui.foodstack;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 30/1/18.
 */

@Singleton
public class FoodStackPresenterImpl implements FoodStackContract.FoodStackPresenter {

    private FoodStackRouterImpl mFoodStackRouter;

    private FoodStackInteractor mFoodStackInteractor;

    @Nullable
    private FoodStackContract.FoodStackView mFoodStackView;


    public FoodStackPresenterImpl(FoodStackInteractor foodStackInteractor, FoodStackRouterImpl foodStackRouter) {
        this.mFoodStackInteractor = foodStackInteractor;
        this.mFoodStackRouter = foodStackRouter;
    }

    @Override
    public void addView(FoodStackContract.FoodStackView view) {
        this.mFoodStackView = view;
        mFoodStackRouter.setView(view);
        mFoodStackInteractor.setContext(view);
    }

    @Override
    public void dropView() {
        mFoodStackView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mFoodStackRouter.presentScreen(screen);

    }

    @Override
    public void getCuisinePhotos( SelectedCuisineList selectedCuisineList) {
        mFoodStackInteractor.getCuisinePhotos( selectedCuisineList);
    }

    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mFoodStackView) {
            switch (result) {
                case SUCCESS:
                    mFoodStackView.success(value);
                    break;
                case FAILURE:
                    mFoodStackView.error(value);
                    break;
                case NONETWORK:
                    mFoodStackView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mFoodStackView.networkError(value);
                    break;
            }
        }
    }
}
