package com.snapxeats.ui.restaurantInfo;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@ActivityScoped
@Subcomponent(modules = RestaurantInfoModule.class)
public interface RestaurantInfoComponent extends AndroidInjector<RestaurantInfoActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RestaurantInfoActivity> {
    }
}