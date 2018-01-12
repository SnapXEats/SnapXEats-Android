package com.example.synerzip.snapxeats.dagger;

import com.example.synerzip.snapxeats.ui.login.LoginActivity;
import com.example.synerzip.snapxeats.ui.login.LoginModule;
import com.example.synerzip.snapxeats.ui.home.HomeActivity;
import com.example.synerzip.snapxeats.ui.home.HomeModule;
import com.example.synerzip.snapxeats.ui.location.LocationActivity;
import com.example.synerzip.snapxeats.ui.location.LocationModule;
import com.example.synerzip.snapxeats.ui.preferences.PreferenceActivity;
import com.example.synerzip.snapxeats.ui.preferences.PreferenceModule;

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