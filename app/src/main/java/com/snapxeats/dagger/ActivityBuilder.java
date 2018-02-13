package com.snapxeats.dagger;

import com.snapxeats.ui.foodstack.FoodStackActivity;
import com.snapxeats.ui.foodstack.FoodStackModule;
import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.login.LoginModule;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.home.HomeModule;
import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.location.LocationModule;
import com.snapxeats.ui.navpreference.NavPrefModule;
import com.snapxeats.ui.navpreference.NavPreferenceActivity;
import com.snapxeats.ui.preferences.PreferenceActivity;
import com.snapxeats.ui.preferences.PreferenceModule;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsModule;
import com.snapxeats.ui.splash.SplashActivity;
import com.snapxeats.ui.splash.SplashModule;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;
import com.snapxeats.ui.restaurant.RestaurantDetailsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Module
public abstract class ActivityBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity bindHomeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PreferenceModule.class)
    abstract PreferenceActivity bindPreferenceActivity();

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

    @ContributesAndroidInjector(modules = NavPrefModule.class)
    abstract NavPreferenceActivity bindNavPrefActivity();
}