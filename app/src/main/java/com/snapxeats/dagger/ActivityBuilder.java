package com.snapxeats.dagger;

import com.snapxeats.ui.login.LoginActivity;
import com.snapxeats.ui.login.LoginModule;
import com.snapxeats.ui.home.HomeActivity;
import com.snapxeats.ui.home.HomeModule;
import com.snapxeats.ui.location.LocationActivity;
import com.snapxeats.ui.location.LocationModule;
import com.snapxeats.ui.preferences.PreferenceActivity;
import com.snapxeats.ui.preferences.PreferenceModule;

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

    @ActivityScoped
    @ContributesAndroidInjector(modules = PreferenceModule.class)
    abstract PreferenceActivity bindPreferenceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LocationModule.class)
    abstract LocationActivity bindLocationActivity();

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity bindLoginActivity();
}