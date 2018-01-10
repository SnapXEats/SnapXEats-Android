package com.example.synerzip.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;

import com.example.synerzip.snapxeats.ui.HomeActivity;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 8/1/18.
 */
public class LoginRouter {

    private Activity mActivity;

    public LoginRouter(Activity activity) {
        this.mActivity = activity;
    }

    @Inject
    public LoginRouter() {
    }

    public void presentScreen() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        mActivity.startActivity(intent);
    }
}
