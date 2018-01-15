package com.example.synerzip.snapxeats.ui.login;

import com.example.synerzip.snapxeats.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 5/1/18.
 */
@Module
public abstract class LoginModule {
    @Provides
    static LoginContract.Presenter presenter( LoginInteractor interactor,LoginRouter loginRouter) {
        LoginContract.Presenter  presenter = new LoginPresenter(interactor,loginRouter);
        interactor.setPresenter(presenter);
        return presenter;
    }
}
