package com.example.synerzip.snapxeats.dagger;

import com.example.synerzip.snapxeats.ui.HomeActivity;
import com.example.synerzip.snapxeats.ui.home.HomeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
@Module
public abstract class ActivityBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity bindHomeActivity();
}
