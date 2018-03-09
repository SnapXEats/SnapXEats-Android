package com.snapxeats.ui.login;

import android.support.annotation.Nullable;

import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.utilities.SnapXResult;

import javax.inject.Singleton;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

@Singleton
public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private LoginRouterImpl mLoginRouter;

    @Nullable
    private LoginContract.LoginView mLoginLoginView;

    private LoginInteractor mLoginInteractor;

    public LoginPresenterImpl(LoginInteractor loginInteractor, LoginRouterImpl loginRouter) {
        this.mLoginInteractor = loginInteractor;
        this.mLoginRouter = loginRouter;
    }

    @Override
    public void addView(LoginContract.LoginView loginView) {
        this.mLoginLoginView = loginView;
        mLoginRouter.setView(loginView);
        mLoginInteractor.setContext(loginView);
    }

    @Override
    public void dropView() {
        mLoginLoginView = null;
    }

    @Override
    public void presentScreen(Router.Screen screen) {
        mLoginRouter.presentScreen(screen);
    }

    @Override
    public void getInstaInfo(String token) {
        mLoginInteractor.getInstaInfo(token);
    }

    @Override
    public void getUserdata(SnapXUserRequest snapXUserRequest) {
        mLoginInteractor.getUserData(snapXUserRequest);
    }
    @Override
    public void response(SnapXResult result, Object value) {
        if (null != mLoginLoginView) {
            switch (result) {
                case SUCCESS:
                    mLoginLoginView.success(value);
                    break;
                case FAILURE:
                    mLoginLoginView.error(value);
                    break;
                case NONETWORK:
                    mLoginLoginView.noNetwork(value);
                    break;
                case NETWORKERROR:
                    mLoginLoginView.networkError(value);
                    break;
            }
        }
    }
}
