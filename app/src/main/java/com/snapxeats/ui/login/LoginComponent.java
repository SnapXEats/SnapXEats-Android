package com.snapxeats.ui.login;

import com.snapxeats.dagger.ActivityScoped;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Prajakta Patil on 5/1/18.
 */

@ActivityScoped
@Subcomponent(modules = LoginModule.class)

public interface LoginComponent extends AndroidInjector<LoginActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<LoginActivity> {
    }
}

