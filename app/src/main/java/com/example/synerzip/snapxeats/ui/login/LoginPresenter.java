package com.example.synerzip.snapxeats.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.example.synerzip.snapxeats.R;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginInteractor mLoginInteractor;

    private LoginRouter mLoginRouter;

    @Nullable
    private LoginContract.View mLoginView;

    @Inject
    public LoginPresenter(LoginInteractor loginInteractor, LoginRouter loginRouter) {
        this.mLoginInteractor = loginInteractor;
        this.mLoginRouter = loginRouter;
    }

    @Override
    public void takeView(LoginContract.View view) {
        this.mLoginView = view;
    }

    @Override
    public void dropView() {
        mLoginView = null;
    }


    public void loginWithInstagram() {
        mLoginInteractor.setLoginView(mLoginView);
        mLoginInteractor.loginWithInstagram();
    }

    @Override
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mLoginView.getActivity());
        builder.setMessage(mLoginView.getActivity().getString(R.string.check_network))
                .setPositiveButton(mLoginView.getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void setRouter() {
        mLoginRouter = new LoginRouter(mLoginView.getActivity());
    }

    @Override
    public void presentScreen() {
        mLoginRouter.presentScreen();
    }
}
