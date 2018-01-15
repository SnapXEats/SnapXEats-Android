package com.snapxeats.common;

import android.app.Activity;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class Router {

    private Activity mActivity;

    /**
     * This method should be called whenever the foreground getActivity changes, so that the
     * always contains the latest getActivity which the user is interacting with
     */
    public void setActivity(Activity activity) {
        mActivity = activity;
    }
}
