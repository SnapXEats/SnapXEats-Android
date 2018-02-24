package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;
import com.snapxeats.common.Router;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 13/2/18.
 */
@Singleton
public class CuisinePrefRouterImpl implements CuisinePrefContract.CuisinePrefRouter{
    private Activity activity;
    private Router router;

    public CuisinePrefRouterImpl(Router router) {
        this.router = router;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    @Override
    public void setView(CuisinePrefContract.CuisinePrefView view) {
        this.activity = view.getActivity();
    }
}
