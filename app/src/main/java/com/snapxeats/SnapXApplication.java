package com.snapxeats;

import com.snapxeats.dagger.AppComponent;
import com.snapxeats.dagger.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class SnapXApplication extends DaggerApplication {

    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("snapxeats.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}

