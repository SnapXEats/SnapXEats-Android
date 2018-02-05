package com.snapxeats.ui.restaurant;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
@ActivityScoped
@Subcomponent(modules = RestaurantDetailsModule.class)
public interface RestaurantDetailsComponent extends AndroidInjector<RestaurantDetailsActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RestaurantDetailsActivity> {
    }
}
