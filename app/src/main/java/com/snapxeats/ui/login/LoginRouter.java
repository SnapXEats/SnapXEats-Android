package com.snapxeats.ui.login;

import android.app.Activity;
import com.snapxeats.common.Router;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 8/1/18.
 */
@Singleton
public class LoginRouter {

    private Activity mActivity;
    private Router router;

    @Inject
    public LoginRouter(Router router) {
        this.router = router;
    }


    public void presentScreen(Router.Screen screen) {
     router.presentScreen(screen);
    }

    public void setView(LoginContract.LoginView loginView) {
        this.mActivity = loginView.getActivity();
    }

}
