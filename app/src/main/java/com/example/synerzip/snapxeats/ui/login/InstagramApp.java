package com.example.synerzip.snapxeats.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.SnapXToast;
import com.example.synerzip.snapxeats.common.constants.WebConstants;
import com.example.synerzip.snapxeats.ui.preferences.PreferenceActivity;

/**
 * Created by Prajakta Patil on 11/1/18.
 */

public class InstagramApp {

    private InstagramDialog mDialog;
    private ProgressDialog mProgress;
    private Context context;
    public static String mCallbackUrl = "";

    public InstagramApp(Context context, String clientId, String clientSecret,
                        String callbackUrl) {

        String mClientId = clientId;
        String mClientSecret = clientSecret;
        this.context = context;
        mCallbackUrl = callbackUrl;

        String mAuthUrl = WebConstants.INSTA_AUTH_URL
                + context.getString(R.string.insta_client_id)
                + clientId
                + context.getString(R.string.insta_redirect_url)
                + mCallbackUrl
                + context.getString(R.string.insta_callback_url);

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                SnapXToast.showToast(InstagramApp.this.context, context.getString(R.string.insta_login_success));
                context.startActivity(new Intent(context, PreferenceActivity.class));
            }

            @Override
            public void onError(String error) {

            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    public void setListener(OAuthAuthenticationListener listener) {
        OAuthAuthenticationListener mListener = listener;
    }

    public void authorize() {
        mDialog.show();
    }

    public interface OAuthAuthenticationListener {
        void onSuccess();

        void onFail(String error);
    }
}
