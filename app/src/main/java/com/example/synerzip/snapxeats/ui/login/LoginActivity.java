package com.example.synerzip.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.example.synerzip.snapxeats.BaseActivity;
import com.example.synerzip.snapxeats.BuildConfig;
import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.WebConstants;
import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;
import com.example.synerzip.snapxeats.common.utilities.SnapXResult;
import com.example.synerzip.snapxeats.dagger.AppContract;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginActivity extends BaseActivity implements LoginContract.LoginView, AppContract.SnapXResponse {
    @Inject
    LoginLoginPresenter mLoginPresenter;

    private CallbackManager mCallbackManager;

    @BindView(R.id.btn_facebook_login)
    protected LoginButton mBtnFbLogin;

    @BindView(R.id.btn_fb_custom)
    protected Button mBtnFb;

    private InstagramApp mApp;

    @BindView(R.id.txt_version)
    protected TextView mTxtVersion;

    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();

    }
    public void initView() {
        mLoginPresenter.addView(this);
        mCallbackManager = CallbackManager.Factory.create();
            loginWithFacebook();
        //TODO manage instagram session
        initInstagram();
        setVersionAndBuildLabel();
    }
    private void setVersionAndBuildLabel() {
        versionName = "V " + BuildConfig.VERSION_NAME + " " + getString(R.string.build)
                + " " + BuildConfig.VERSION_CODE;
        if (BuildConfig.BUILD_CAPTION) {
            mTxtVersion.setVisibility(View.VISIBLE);
            mTxtVersion.setText(versionName);
        } else {
            mTxtVersion.setVisibility(View.GONE);
        }
    }
    private void loginWithFacebook() {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mLoginPresenter.response(SnapXResult.SUCCESS);
                    }

                    @Override
                    public void onCancel() {
                        mLoginPresenter.response(SnapXResult.NETWORKERROR);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        mLoginPresenter.response(SnapXResult.NETWORKERROR);
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
                showNetworkErrorDialog(null);
            }
        }
    }

    @OnClick(R.id.btn_instagram_login)
    public void btnLoginInstagram(View view) {
        if (NetworkUtility.isNetworkAvailable(this)) {
            mApp.authorize();
        } else {
            showNetworkErrorDialog(null);
        }
    }

    private void initInstagram() {
        mApp = new InstagramApp(this, WebConstants.INSTA_CLIENT_ID, WebConstants.INSTA_CLIENT_SECRET,
                WebConstants.INSTA_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                mLoginPresenter.response(SnapXResult.SUCCESS);
            }

            @Override
            public void onFail(String error) {
                mLoginPresenter.response(SnapXResult.NETWORKERROR);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void success() {
        mLoginPresenter.presentScreen();
    }

    @Override
    public void error() {

    }

    @Override
    public void noNetwork() {
        showNetworkErrorDialog(null);
    }

    @Override
    public void networkError() {

    }
}

