package com.example.synerzip.snapxeats.ui.login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 5/1/18.
 */
@Module
public abstract class LoginModule {

    @Provides
    static LoginContract.LoginPresenter presenter(LoginInteractor interactor, LoginRouter loginRouter) {
        LoginContract.LoginPresenter loginPresenter = new LoginPresenterImpl(interactor,loginRouter);
        interactor.setPresenter(loginPresenter);
        return loginPresenter;
    }
}
