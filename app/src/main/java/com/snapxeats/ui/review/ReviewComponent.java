package com.snapxeats.ui.review;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 12/4/18.
 */
@ActivityScoped
@Subcomponent(modules = ReviewModule.class)
public interface ReviewComponent extends AndroidInjector<ReviewActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ReviewActivity> {
    }
}