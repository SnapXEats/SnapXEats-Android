package com.snapxeats.ui.maps;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 27/3/18.
 */
public class MapsRouterImpl implements MapsContract.MapsRouter {
    private Activity mActivity;
    private Router router;

    @Inject
    public MapsRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    public void setView(MapsContract.MapsView view) {
        this.mActivity = view.getActivity();
    }
}
