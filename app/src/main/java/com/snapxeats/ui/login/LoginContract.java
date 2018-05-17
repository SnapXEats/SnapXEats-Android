package com.snapxeats.ui.login;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginContract {

    interface LoginView extends BaseView<LoginPresenter>, AppContract.SnapXResults {
    }

    interface LoginPresenter extends BasePresenter<LoginView> {

        void presentScreen(Router.Screen screen);

        void getInstaInfo(String token);

        void getUserdata(SnapXUserRequest snapXUserRequest);
    }

    public interface LoginRouter {

        void presentScreen(Router.Screen screen);

        void setView(LoginContract.LoginView view);
    }
}
