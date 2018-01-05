package com.example.synerzip.snapxeats.ui.preferences;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Module
public abstract class PreferenceModule {

    @Provides
    static PreferenceContract.PreferencePresenter providePresenter(PreferenceInteractor interactor){
        PreferenceContract.PreferencePresenter preferencePresenter=new PreferencePresenter(interactor);
        interactor.setPreferencePresenter(preferencePresenter);
        return preferencePresenter;
    }
}
