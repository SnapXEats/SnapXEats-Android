package com.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Intent;

import com.snapxeats.common.Router;
import com.snapxeats.ui.location.LocationActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.snapxeats.common.Router.Screen.LOCATION;

/**
 * Created by Snehal Tembare on 8/1/18.
 */

@Singleton
public class PreferenceRouterImpl implements PreferenceContract.PreferenceRouter {

    private Activity activity;
    private Router router;

    @Inject
    public PreferenceRouterImpl(Router router) {
        this.router = router;
    }


    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    /**
     * Set view to Presenter
     *
     * @param view
     */
    @Override
    public void setView(PreferenceContract.PreferenceView view) {
        this.activity = view.getActivity();
    }
}
