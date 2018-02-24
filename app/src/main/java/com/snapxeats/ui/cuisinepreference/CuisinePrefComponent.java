package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.dagger.ActivityScoped;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

@ActivityScoped
@Subcomponent(modules = CuisinePrefModule.class)
public interface CuisinePrefComponent extends AndroidInjector<CuisinePrefActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<CuisinePrefActivity> {
    }
}
