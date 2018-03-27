package com.snapxeats.ui.directions;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
public class DirectionsRouterImpl implements DirectionsContract.DirectionsRouter {
    private Activity mActivity;
    private Router router;

    @Inject
    public DirectionsRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    public void setView(DirectionsContract.DirectionsView view) {
        this.mActivity = view.getActivity();
    }
}