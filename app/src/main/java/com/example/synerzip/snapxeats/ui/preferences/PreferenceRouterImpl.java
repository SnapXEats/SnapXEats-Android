package com.example.synerzip.snapxeats.ui.preferences;

import android.app.Activity;
import android.content.Intent;

import com.example.synerzip.snapxeats.ui.location.LocationActivity;

import javax.inject.Inject;

/**
 * Created by Snehal Tembare on 8/1/18.
 */

public class PreferenceRouterImpl implements PreferenceContract.PreferenceRouter {

    private Activity activity;

    @Inject
    public PreferenceRouterImpl() {

    }

    @Override
    public void presentScreen() {
        Intent intent = new Intent(activity, LocationActivity.class);
        activity.startActivity(intent);
    }

   /**
     * Set view to Presenter
     * @param view
     *
     */
    @Override
    public void setView(PreferenceContract.PreferenceView view) {
        this.activity = view.getActivity();
    }
}
