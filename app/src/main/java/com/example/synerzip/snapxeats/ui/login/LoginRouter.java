package com.example.synerzip.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;

import com.example.synerzip.snapxeats.ui.preferences.PreferenceActivity;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 8/1/18.
 */
public class LoginRouter {

    private Activity mActivity;

    @Inject
    public LoginRouter() {
    }

    public void presentScreen() {
        Intent intent = new Intent(mActivity, PreferenceActivity.class);
        mActivity.startActivity(intent);
    }

    public void setView(LoginContract.LoginView loginView) {
        this.mActivity = loginView.getActivity();
    }

}
