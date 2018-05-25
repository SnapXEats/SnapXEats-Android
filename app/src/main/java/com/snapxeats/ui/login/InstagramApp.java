package com.snapxeats.ui.login;

import android.app.ProgressDialog;
import android.content.Context;

import com.snapxeats.R;
import com.snapxeats.common.constants.WebConstants;

/**
 * Created by Prajakta Patil on 11/1/18.
 */

public class InstagramApp {
    private InstagramDialog instagramDialog;
    private Context context;
    public static String mCallbackUrl = "";

    public InstagramApp(Context context, String clientId, String callbackUrl) {
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
            }

            @Override
            public void onError(String error) {
            }
        };
        instagramDialog = new InstagramDialog(context, mAuthUrl, listener);
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    public void setListener(OAuthAuthenticationListener listener) {
        OAuthAuthenticationListener mListener = listener;
    }

    public void authorize() {
        instagramDialog.show();
    }

    public interface OAuthAuthenticationListener {
        void onSuccess();

        void onFail(String error);
    }
}
