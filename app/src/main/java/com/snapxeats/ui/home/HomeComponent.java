package com.snapxeats.ui.home;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 4/1/18.
 */

@ActivityScoped
@Subcomponent(modules = HomeModule.class)

public interface HomeComponent extends AndroidInjector<HomeActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<HomeActivity> {
    }
}

