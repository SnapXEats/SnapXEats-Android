package com.snapxeats.common;

import android.app.Activity;
import android.content.Intent;

import com.snapxeats.ui.foodstack.FoodStackActivity;
import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.preferences.PreferenceActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.snapxeats.ui.preferences.PreferenceActivity.PreferenceConstant.CUSTOM_LOCATION;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Singleton
public class Router {
    public enum Screen {
        LOCATION, PREFERENCE, FOODSTACK,RESTAURANT_DETAILS;
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
            case FOODSTACK:
                presentFoodStackScreen();
                break;
            case PREFERENCE:
                presentPreferenceScreen();
                break;
            case RESTAURANT_DETAILS:
                presentRestaurantDetailsScreen();
                break;
        }
    }

    private void presentPreferenceScreen() {
        mActivity.startActivity(new Intent(mActivity, PreferenceActivity.class));
    }

    private void presentLocationScreen() {
        mActivity.startActivityForResult(new Intent(mActivity, LocationActivity.class),CUSTOM_LOCATION);
    }

    private void presentFoodStackScreen() {
        mActivity.startActivity(new Intent(mActivity, FoodStackActivity.class));
    }
    private void presentRestaurantDetailsScreen() {
        mActivity.startActivity(new Intent(mActivity, RestaurantDetailsActivity.class));
    }
}
