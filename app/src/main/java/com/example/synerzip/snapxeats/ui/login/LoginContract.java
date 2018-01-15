package com.example.synerzip.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;

import com.example.synerzip.snapxeats.BasePresenter;
import com.example.synerzip.snapxeats.BaseView;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginContract{

    interface View extends BaseView<Presenter> {
        Activity getActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void setRouter();
        void presentScreen();
        void loginWithInstagram();
        void showAlertDialog();
    }
}
