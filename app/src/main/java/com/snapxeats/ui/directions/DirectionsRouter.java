package com.snapxeats.ui.directions;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 22/3/18.
 */

@Singleton
public class DirectionsRouter {
    private Activity mActivity;
    private Router mRouter;

    public DirectionsRouter(Router mRouter){
        this.mRouter=mRouter;
    }

    public void presentScreen(Router.Screen screen) {
        mRouter.presentScreen(screen);
    }

    public void setView(DirectionsContract.DirectionsView view) {
        this.mActivity = view.getActivity();
    }
}
