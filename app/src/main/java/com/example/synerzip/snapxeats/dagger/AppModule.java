package com.example.synerzip.snapxeats.dagger;

import android.app.Application;
import android.content.Context;

import com.example.synerzip.snapxeats.common.Router;
import com.example.synerzip.snapxeats.ui.login.LoginComponent;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
@Module(subcomponents = LoginComponent.class)
public abstract class AppModule {

    @Provides
    @Singleton
    static Router provideRouter() {
        return new Router();
    }

    @Provides
    static Context provideAppContext(Context context) {
        return context;
    }

    @Binds
    abstract Context provideContext(Application application);
}