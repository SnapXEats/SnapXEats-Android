package com.snapxeats.common;

import android.app.Activity;
import android.content.Intent;

import com.snapxeats.ui.cuisinepreference.CuisinePrefActivity;
import com.snapxeats.ui.directions.DirectionsActivity;
import com.snapxeats.ui.foodpreference.FoodPreferenceActivity;
import com.snapxeats.ui.foodstack.FoodStackActivity;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.maps.MapsActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.review.ReviewActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Singleton
public class Router {
    public enum Screen {
        LOCATION, HOME, FOODSTACK, RESTAURANT_DETAILS, CUISINE_PREF, FOOD_PREF, DIRECTIONS, MAPS, LOGIN,
        REVIEW
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
            case HOME:
                presentPreferenceScreen();
                break;
            case RESTAURANT_DETAILS:
                presentRestaurantDetailsScreen();
                break;

            case CUISINE_PREF:
                presentCuisinePrefScreen();
                break;
            case FOOD_PREF:
                presentFoodPrefScreen();
                break;

            case DIRECTIONS:
                presentDirectionsScreen();
                break;

            case MAPS:
                presentMapsScreen();
                break;

            case LOGIN:
                presentLogInScreen();
                break;

            case REVIEW:
                presentReviewScreen();
                break;
        }
    }

    private void presentPreferenceScreen() {
        mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
    }

    private void presentLocationScreen() {
        mActivity.startActivity(new Intent(mActivity, LocationActivity.class));
    }

    private void presentFoodStackScreen() {
        mActivity.startActivity(new Intent(mActivity, FoodStackActivity.class));
    }

    private void presentRestaurantDetailsScreen() {
        mActivity.startActivity(new Intent(mActivity, RestaurantDetailsActivity.class));
    }

    private void presentCuisinePrefScreen() {
        mActivity.startActivity(new Intent(mActivity, CuisinePrefActivity.class));
    }

    private void presentFoodPrefScreen() {
        mActivity.startActivity(new Intent(mActivity, FoodPreferenceActivity.class));
    }

    private void presentDirectionsScreen() {
        mActivity.startActivity(new Intent(mActivity, DirectionsActivity.class));
    }

    private void presentMapsScreen() {
        mActivity.startActivity(new Intent(mActivity, MapsActivity.class));
    }

    private void presentReviewScreen() {
        mActivity.startActivity(new Intent(mActivity, ReviewActivity.class));
    }

    private void presentLogInScreen() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
    }
}
