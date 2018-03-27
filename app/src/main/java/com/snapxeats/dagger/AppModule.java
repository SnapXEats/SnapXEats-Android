package com.snapxeats.dagger;

import android.app.Application;
import android.content.Context;

import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.ui.cuisinepreference.CuisinePrefComponent;
import com.snapxeats.ui.directions.DirectionsComponent;
import com.snapxeats.ui.directions.DirectionsModule;
import com.snapxeats.ui.foodpreference.FoodPrefComponent;
import com.snapxeats.ui.foodstack.FoodStackComponent;
import com.snapxeats.ui.location.LocationComponent;
import com.snapxeats.ui.login.LoginComponent;
import com.snapxeats.ui.home.HomeComponent;
import com.snapxeats.ui.maps.MapsComponent;
import com.snapxeats.ui.restaurant.RestaurantDetailsComponent;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Module(subcomponents = {LoginComponent.class,
        HomeComponent.class,
        FoodStackComponent.class,
        LocationComponent.class,
        RestaurantDetailsComponent.class,
        HomeComponent.class,
        CuisinePrefComponent.class,
        FoodPrefComponent.class,
        DirectionsComponent.class,
        MapsComponent.class})

abstract class AppModule {

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