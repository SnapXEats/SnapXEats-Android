package com.example.synerzip.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mLoginPresenter;

    private CallbackManager mCallbackManager;

    @BindView(R.id.btn_facebook_login)
    protected LoginButton mBtnFbLogin;

    @BindView(R.id.btn_fb_custom)
    protected Button mBtnFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mLoginPresenter.takeView(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mLoginPresenter.setRouter();
        loginWithFacebook();
    }

    public void loginWithFacebook(){
        mCallbackManager = CallbackManager.Factory.create();
        mBtnFbLogin.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mLoginPresenter.presentScreen();

                        //facebook access token for future reference
                        loginResult.getAccessToken().getToken();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }

    @OnClick(R.id.txt_login_skip)
    public void txtLoginSkip(View view) {
        mLoginPresenter.presentScreen();
    }

    @OnClick(R.id.btn_fb_custom)
    public void btnFacebook(View view) {
        if (view == mBtnFb) {
            if (NetworkUtility.isNetworkAvailable(this)) {
                mBtnFbLogin.performClick();
            } else {
                mLoginPresenter.showAlertDialog();
            }
        }
    }

    @OnClick(R.id.btn_instagram_login)
    public void btnLoginInsta(View view) {
        if (NetworkUtility.isNetworkAvailable(this)) {
            mLoginPresenter.loginWithInstagram();
            mLoginPresenter.presentScreen();
        } else {
            mLoginPresenter.showAlertDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager = CallbackManager.Factory.create();
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        mLoginPresenter.dropView();
        super.onDestroy();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}

