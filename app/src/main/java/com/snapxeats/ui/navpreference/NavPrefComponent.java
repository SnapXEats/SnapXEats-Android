package com.snapxeats.ui.navpreference;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

@ActivityScoped
@Subcomponent(modules = NavPrefModule.class)
public interface NavPrefComponent extends AndroidInjector<NavPreferenceActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<NavPreferenceActivity> {
    }
}
