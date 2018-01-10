package com.example.synerzip.snapxeats.ui.preferences;

import com.example.synerzip.snapxeats.dagger.ActivityScoped;
import com.example.synerzip.snapxeats.dagger.AppComponent;

import dagger.Component;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Snehal Tembare on 4/1/18.
 */

@ActivityScoped
@Subcomponent(modules = PreferenceModule.class)

public interface PreferenceComponent extends AndroidInjector<PreferenceActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PreferenceActivity>{}
}

