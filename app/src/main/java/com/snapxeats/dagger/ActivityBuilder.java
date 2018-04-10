package com.snapxeats.dagger;

import com.snapxeats.ui.cuisinepreference.CuisinePrefActivity;
import com.snapxeats.ui.cuisinepreference.CuisinePrefModule;
import com.snapxeats.ui.directions.DirectionsActivity;
import com.snapxeats.ui.directions.DirectionsModule;
import com.snapxeats.ui.foodpreference.FoodPrefModule;
import com.snapxeats.ui.foodpreference.FoodPreferenceActivity;
import com.snapxeats.ui.foodstack.FoodStackActivity;
import com.snapxeats.ui.foodstack.FoodStackModule;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.home.HomeModule;
import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.location.LocationModule;
import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.login.LoginModule;
import com.snapxeats.ui.maps.MapsActivity;
import com.snapxeats.ui.maps.MapsModule;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsModule;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoActivity;
import com.snapxeats.ui.restaurantInfo.RestaurantInfoModule;
import com.snapxeats.ui.splash.SplashActivity;
import com.snapxeats.ui.splash.SplashModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Module
public abstract class ActivityBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity bindPreferenceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LocationModule.class)
    abstract LocationActivity bindLocationActivity();

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = FoodStackModule.class)
    abstract FoodStackActivity bindFoodStackActivity();

    @ContributesAndroidInjector(modules = RestaurantDetailsModule.class)
    abstract RestaurantDetailsActivity bindRestaurantDetailsActivity();

    @ContributesAndroidInjector(modules = SplashModule.class)
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = CuisinePrefModule.class)
    abstract CuisinePrefActivity bindCuisinePrefActivity();

    @ContributesAndroidInjector(modules = FoodPrefModule.class)
    abstract FoodPreferenceActivity bindFoodPrefActivity();

    @ContributesAndroidInjector(modules = RestaurantInfoModule.class)
    abstract RestaurantInfoActivity bindRestaurantInfoActivity();

    @ContributesAndroidInjector(modules = DirectionsModule.class)
    abstract DirectionsActivity bindDirectionsActivity();

    @ContributesAndroidInjector(modules = MapsModule.class)
    abstract MapsActivity bindMapsActivity();
}
