package com.snapxeats.ui.foodpreference;

import com.snapxeats.dagger.ActivityScoped;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

@ActivityScoped
@Subcomponent(modules = FoodPrefModule.class)
public interface FoodPrefComponent extends AndroidInjector<FoodPreferenceActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FoodPreferenceActivity> {
    }
}