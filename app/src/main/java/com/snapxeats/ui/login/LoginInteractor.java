package com.snapxeats.ui.login;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

@Singleton
public class LoginInteractor {

    private LoginContract.LoginView mLoginLoginView;

    @Inject
    public LoginInteractor() {
    }

    public void setLoginView(LoginContract.LoginView loginLoginView) {
        this.mLoginLoginView = loginLoginView;
    }

    public void setPresenter(LoginContract.LoginPresenter loginPresenter) {
        LoginContract.LoginPresenter mLoginLoginPresenter = loginPresenter;
    }

}



