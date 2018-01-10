package com.example.synerzip.snapxeats.dagger;

import android.app.Application;

import com.example.synerzip.snapxeats.SnapXApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class})

public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(SnapXApplication app);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
