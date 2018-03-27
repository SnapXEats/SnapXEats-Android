package com.snapxeats.ui.maps;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 27/3/18.
 */

@ActivityScoped
@Subcomponent(modules = MapsModule.class)

public interface MapsComponent extends AndroidInjector<MapsActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapsActivity> {
    }
}
