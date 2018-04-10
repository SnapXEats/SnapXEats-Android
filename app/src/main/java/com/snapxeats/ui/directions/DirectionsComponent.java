package com.snapxeats.ui.directions;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
@ActivityScoped
@Subcomponent(modules = DirectionsModule.class)

public interface DirectionsComponent extends AndroidInjector<DirectionsActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DirectionsActivity> {
    }
}