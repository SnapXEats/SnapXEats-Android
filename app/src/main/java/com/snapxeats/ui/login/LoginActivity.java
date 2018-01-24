package com.snapxeats.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.snapxeats.BaseActivity;
import com.snapxeats.BuildConfig;
import com.snapxeats.R;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.dagger.AppContract;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.hockeyapp.android.utils.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.snapxeats.common.Router.Screen.PREFERENCE;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginActivity extends BaseActivity implements LoginContract.LoginView, AppContract.SnapXResults {
    private static final String TAG = "LoginActivity";
    @Inject
    LoginPresenterImpl mLoginPresenter;

    private CallbackManager mCallbackManager;

    @BindView(R.id.btn_facebook_login)
    protected LoginButton mBtnFbLogin;

    @BindView(R.id.btn_fb_custom)
    protected Button mBtnFb;

    private InstagramApp mApp;

    @BindView(R.id.txt_version)
    protected TextView mTxtVersion;

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
        getFbHashKey(this);
        loginWithFacebook();
        //TODO manage instagram session
        initInstagram();
        setVersionAndBuildLabel();
    }

    //get facebook hash key for exachanging information between app and Facebook
    public String getFbHashKey(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.d("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d(TAG, e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.toString());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return key;
    }

    private void setVersionAndBuildLabel() {
        String versionName = "V " + BuildConfig.VERSION_NAME + " " + getString(R.string.build)
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
    public void txtLoginSkip() {
        mLoginPresenter.presentScreen(PREFERENCE);
    }

    @OnClick(R.id.btn_fb_custom)
    public void btnFacebook(View view) {
        if (view == mBtnFb) {
            if (NetworkUtility.isNetworkAvailable(this)) {
                //access token kept null for now ; as fb logout functionality will be implemented in later screen
                AccessToken.setCurrentAccessToken(null);
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
        mLoginPresenter.presentScreen(PREFERENCE);
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

