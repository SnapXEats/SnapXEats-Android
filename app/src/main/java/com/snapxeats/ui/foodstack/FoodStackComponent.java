package com.snapxeats.ui.foodstack;

import com.snapxeats.dagger.ActivityScoped;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@ActivityScoped
@Subcomponent(modules = FoodStackModule.class)
public interface FoodStackComponent  extends AndroidInjector<FoodStackActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FoodStackActivity> {
    }
}
