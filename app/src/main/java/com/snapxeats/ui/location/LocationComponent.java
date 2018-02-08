package com.snapxeats.ui.location;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

@ActivityScoped
@Subcomponent(modules = LocationModule.class)

public interface LocationComponent extends AndroidInjector<LocationActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LocationActivity>{}
}
