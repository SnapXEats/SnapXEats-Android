package com.snapxeats.common;

import android.app.Activity;
import android.content.Intent;

import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.preferences.PreferenceActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Singleton
public class Router {

    public enum Screen {
        LOGIN, LOCATION, FOODSTACK, PREFERENCE
    }

    private Activity mActivity;

    @Inject
    public Router() {
    }

    /**
     * This method should be called whenever the foreground getActivity changes, so that the
     * always contains the latest getActivity which the user is interacting with
     */
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void presentScreen(Screen screen) {
        switch (screen) {
            case LOCATION:
                presentLocationScreen();
                break;
        }
    }

    public void presentLocationScreen() {
        mActivity.startActivity(new Intent(mActivity, LocationActivity.class));
    }
}
