package com.snapxeats.ui.login;

import android.app.Activity;

import com.snapxeats.common.Router;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 24/1/18.
 */
public class LoginRouterImpl implements LoginContract.LoginRouter {
    private Activity mActivity;
    private Router router;

    @Inject
    public LoginRouterImpl(Router router) {
        this.router = router;
    }

    public void presentScreen(Router.Screen screen) {
        router.presentScreen(screen);
    }

    public void setView(LoginContract.LoginView loginView) {
        this.mActivity = loginView.getActivity();
    }
}
