package com.example.synerzip.snapxeats.ui.login;

import android.support.annotation.Nullable;

import com.example.synerzip.snapxeats.common.utilities.SnapXResult;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

public class LoginLoginPresenter implements LoginContract.LoginPresenter {

    private LoginRouter mLoginRouter;

    @Nullable
    private LoginContract.LoginView mLoginLoginView;

    @Inject
    public LoginLoginPresenter(LoginInteractor loginInteractor, LoginRouter loginRouter) {
        LoginInteractor mLoginInteractor = loginInteractor;
        this.mLoginRouter = loginRouter;
    }

    @Override
    public void addView(LoginContract.LoginView loginView) {
        this.mLoginLoginView = loginView;
        mLoginRouter.setView(loginView);
    }

    @Override
    public void dropView() {
        mLoginLoginView = null;
    }

    @Override
    public void presentScreen() {
        mLoginRouter.presentScreen();
    }

    @Override
    public void response(SnapXResult result) {
        switch(result) {
            case SUCCESS:
                mLoginLoginView.success();
                break;
            case FAILURE:
                mLoginLoginView.error();
                break;
            case NONETWORK:
                mLoginLoginView.noNetwork();
                break;
            case NETWORKERROR:
                mLoginLoginView.networkError();
                break;
        }
    }

}
