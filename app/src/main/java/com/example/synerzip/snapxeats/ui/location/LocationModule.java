package com.example.synerzip.snapxeats.ui.location;

import com.example.synerzip.snapxeats.ui.preferences.PreferenceContract;
import com.example.synerzip.snapxeats.ui.preferences.PreferenceInteractor;
import com.example.synerzip.snapxeats.ui.preferences.PreferencePresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

@Module
public abstract class LocationModule {
    @Provides
    static LocationContract.LocationPresenter providePresenter(LocationInteractor interactor){
        LocationContract.LocationPresenter locationPresenter=new LocationPresenterImpl(interactor);
        interactor.setLocationPresenter(locationPresenter);
        return locationPresenter;
    }
}
