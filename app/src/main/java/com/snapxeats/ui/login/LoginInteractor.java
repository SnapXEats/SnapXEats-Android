package com.snapxeats.ui.login;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

@Singleton
public class LoginInteractor {

    private LoginContract.LoginView mLoginLoginView;
    private LoginContract.LoginPresenter mLoginPresenter;

    private Context mContext;

    @Inject
    public LoginInteractor() {
    }

    public void setLoginView(LoginContract.LoginView loginLoginView) {
        mLoginLoginView = loginLoginView;
    }

    public void setPresenter(LoginContract.LoginPresenter loginPresenter) {
        mLoginPresenter = loginPresenter;
    }
}



