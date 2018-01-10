package com.example.synerzip.snapxeats;

import com.example.synerzip.snapxeats.dagger.AppComponent;
import com.example.synerzip.snapxeats.dagger.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class SnapXApplication extends DaggerApplication {

    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}

