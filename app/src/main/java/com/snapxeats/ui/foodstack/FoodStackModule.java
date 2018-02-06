package com.snapxeats.ui.foodstack;

import com.snapxeats.common.Router;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Module
public abstract class FoodStackModule {

    @Provides
    static FoodStackContract.FoodStackPreseneter preseneter(FoodStackInteractor interactor,
                                                            FoodStackRouterImpl foodStackRouter){
        FoodStackContract.FoodStackPreseneter foodStackPreseneter=new FoodStackPresenterImpl(interactor,foodStackRouter);
        interactor.setFoodStackPresenter(foodStackPreseneter);
        return foodStackPreseneter;
    }
    @Provides
    static FoodStackRouterImpl provideFoodStackRouter(Router router){
        FoodStackRouterImpl foodStackRouter=new FoodStackRouterImpl(router);
        return foodStackRouter;
    }
}