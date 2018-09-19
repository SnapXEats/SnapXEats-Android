package com.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.snapxeats.BaseActivity;
import com.snapxeats.BuildConfig;
import com.snapxeats.R;
import com.snapxeats.common.Router;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.utilities.LoginUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.HOME;
import static com.snapxeats.common.constants.UIConstants.SX_VERSION;

/**
 * Created by Prajakta Patil on 4/1/18.
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView,
        AppContract.SnapXResults, InstagramDialog.InstagramDialogListener {

    @Inject
    LoginContract.LoginPresenter mLoginPresenter;

    @Inject
    SnapXDialog mSnapXDialog;

    private CallbackManager mCallbackManager;

    @BindView(R.id.btn_facebook_login)
    protected LoginButton mBtnFbLogin;

    @BindView(R.id.btn_fb_custom)
    protected Button mBtnFb;

    private InstagramApp mApp;

    @BindView(R.id.txt_version)
    protected TextView mTxtVersion;

    @BindView(R.id.login_parent_layout)
    protected ConstraintLayout mParentLayout;

    private SnapXUserRequest snapXUserRequest;

    @Inject
    LoginUtility loginUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {
        mLoginPresenter.addView(this);
        loginUtility.setContext(this);
        mSnapXDialog.setContext(this);
        mCallbackManager = CallbackManager.Factory.create();
        loginUtility.getFbHashKey(this);

        //initialize fb sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        loginWithFacebook();
        setVersionAndBuildLabel();
        initInstagram();

        setVersionAndBuildLabel();

        //permission for getting token in logs
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
    }

    private void setVersionAndBuildLabel() {
        String versionName = SX_VERSION + BuildConfig.VERSION_NAME + " " + getString(R.string.build)
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
                        snapXUserRequest = new SnapXUserRequest(AccessToken.getCurrentAccessToken().getToken(),
                                getString(R.string.platform_facebook), AccessToken.getCurrentAccessToken().getUserId());
                        showProgressDialog();
                        mLoginPresenter.getUserdata(snapXUserRequest);
                    }

                    @Override
                    public void onCancel() {
                        mLoginPresenter.response(SnapXResult.NETWORKERROR, null);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        mLoginPresenter.response(SnapXResult.ERROR, null);
                    }
                });
    }

    @OnClick(R.id.txt_login_skip)
    public void txtLoginSkip() {
        mLoginPresenter.presentScreen(HOME);
    }

    @OnClick(R.id.btn_fb_custom)
    public void btnFacebook(View view) {
        if (view == mBtnFb) {
            if (NetworkUtility.isNetworkAvailable(this)) {
                mBtnFbLogin.performClick();
            } else {
                showNetworkErrorDialog((dialog, which) -> {
                    if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                        AppContract.DialogListenerAction click = () -> {
                            showProgressDialog();
                            mBtnFbLogin.performClick();
                        };
                        showSnackBar(mParentLayout, setClickListener(click));
                    }
                });
            }
        }
    }

    @OnClick(R.id.btn_instagram_login)
    public void btnLoginInstagram(View view) {
        if (NetworkUtility.isNetworkAvailable(this)) {
            mApp.authorize();
        } else {
            showNetworkErrorDialog((dialog, which) -> {
                if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                    AppContract.DialogListenerAction click = () -> {
                        showProgressDialog();
                        mApp.authorize();
                    };
                    showSnackBar(mParentLayout, setClickListener(click));
                }
            });
        }
    }

    private void initInstagram() {
        mApp = new InstagramApp(this, WebConstants.INSTA_CLIENT_ID, WebConstants.INSTA_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                //TODO data passed null for now
                mLoginPresenter.response(SnapXResult.SUCCESS, null);
            }

            @Override
            public void onFail(String error) {
                mLoginPresenter.response(SnapXResult.NETWORKERROR, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
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
    public void success(Object value) {
        dismissProgressDialog();
        //TODO value not used yet
        SnapXUserResponse snapXUserResponse = (SnapXUserResponse) value;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.login_success);
        builder.setMessage(getString(R.string.login_success_msg));
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            mLoginPresenter.presentScreen(Router.Screen.HOME);
        });
        builder.create().show();
    }

    @Override
    public void error(Object value) {
    }

    @Override
    public void noNetwork(Object value) {
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity())) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    mLoginPresenter.getUserdata(snapXUserRequest);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });
    }

    @Override
    public void networkError(Object value) {

    }

    @Override
    public void onReturnValue(String token) {
        showProgressDialog();
        mLoginPresenter.getInstaInfo(token);
    }
}

