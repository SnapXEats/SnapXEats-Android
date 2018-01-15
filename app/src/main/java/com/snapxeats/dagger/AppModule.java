package com.snapxeats.dagger;

import android.app.Application;
import android.content.Context;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.ui.login.LoginComponent;
import com.snapxeats.ui.preferences.PreferenceComponent;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
@Module(subcomponents = {LoginComponent.class,  PreferenceComponent.class})

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

    @Provides
    static SnapXDialog provideSnapXDialog() {
        return new SnapXDialog();
    }

    @Binds
    abstract Context provideContext(Application application);
}