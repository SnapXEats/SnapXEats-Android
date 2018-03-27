package com.snapxeats.ui.maps;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 27/3/18.
 */

@Singleton
public class MapsRouter {
    private Activity mActivity;
    private Router mRouter;

    public MapsRouter(Router mRouter){
        this.mRouter=mRouter;
    }

    public void presentScreen(Router.Screen screen) {
        mRouter.presentScreen(screen);
    }

    public void setView(MapsContract.MapsView view) {
        this.mActivity = view.getActivity();
    }
}
