package com.snapxeats.ui.login;

import com.snapxeats.common.Router;
import com.snapxeats.ui.preferences.PreferenceRouterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Prajakta Patil on 5/1/18.
 */
@Module
public abstract class LoginModule {

    @Provides
    static LoginContract.LoginPresenter presenter(LoginInteractor interactor, LoginRouterImpl loginRouter) {
        LoginContract.LoginPresenter loginPresenter = new LoginPresenterImpl(interactor,loginRouter);
        interactor.setPresenter(loginPresenter);
        return loginPresenter;
    }
    @Provides
    static LoginRouterImpl provideLoginRouter(Router router) {
        LoginRouterImpl loginRouter = new LoginRouterImpl(router);
        return loginRouter;
    }
}
