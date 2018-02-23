package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 8/1/18.
 */

@Singleton
public class HomeFgmtRouterImpl implements HomeFgmtContract.HomeFgmtRouter {

    private Activity activity;
    private Router router;

    @Inject
    public HomeFgmtRouterImpl(Router router) {
        this.router=router;
    }


    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

   /**
     * Set view to Presenter
     * @param view
     *
     */
    @Override
    public void setView(HomeFgmtContract.HomeFgmtView view) {
        this.activity = view.getActivity();
    }
}
