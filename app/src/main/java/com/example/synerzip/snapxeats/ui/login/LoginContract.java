package com.example.synerzip.snapxeats.ui.login;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;
import com.example.synerzip.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginContract {

    interface LoginView extends BaseView<LoginPresenter>, AppContract.SnapXResults {

    }

    interface LoginPresenter extends BasePresenter<LoginView> {
        void presentScreen();
    }
}
