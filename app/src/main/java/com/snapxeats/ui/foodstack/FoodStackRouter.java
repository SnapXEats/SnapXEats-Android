package com.snapxeats.ui.foodstack;

import android.app.Activity;

import com.snapxeats.common.Router;
import com.snapxeats.ui.login.LoginContract;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Singleton
public class FoodStackRouter {
    private Activity mActivity;
    private Router mRouter;

    public FoodStackRouter(Router mRouter){
        this.mRouter=mRouter;
    }

    public void presentScreen(Router.Screen screen) {
        mRouter.presentScreen(screen);
    }

    public void setView(FoodStackContract.FoodStackView view) {
        this.mActivity = view.getActivity();
    }
}
