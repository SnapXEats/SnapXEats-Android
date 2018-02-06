package com.snapxeats.ui.foodstack;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
public class FoodStackContract {

    interface FoodStackView extends BaseView<FoodStackPreseneter>, AppContract.SnapXResults {
    }


    interface FoodStackPreseneter extends BasePresenter<FoodStackView> {

        void presentScreen(Router.Screen screen);

        void getCuisinePhotos(FoodStackContract.FoodStackView foodStackView, SelectedCuisineList selectedCuisineList);
    }

    public interface FoodStackRouter {

        void presentScreen(Router.Screen screen);

        void setView(FoodStackContract.FoodStackView view);
    }
}