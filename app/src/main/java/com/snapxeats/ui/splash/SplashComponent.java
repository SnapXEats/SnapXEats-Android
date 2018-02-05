package com.snapxeats.ui.splash;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 30/1/18.
 */

@ActivityScoped
@Subcomponent(modules = SplashModule.class)
public interface SplashComponent extends AndroidInjector<SplashActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SplashActivity>{}
}