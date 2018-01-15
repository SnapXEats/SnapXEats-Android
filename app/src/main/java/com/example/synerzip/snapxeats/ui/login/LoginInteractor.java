package com.example.synerzip.snapxeats.ui.login;

import android.content.Context;
import android.net.Uri;
import com.example.synerzip.snapxeats.R;
import com.example.synerzip.snapxeats.common.constants.SnapXToast;
import com.example.synerzip.snapxeats.common.constants.WebConstants;
import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 4/1/18.
 */
public class LoginInteractor {

    private LoginContract.View mLoginView;

    @Inject
    public LoginInteractor() {
    }
    public void setLoginView(LoginContract.View loginView) {
        this.mLoginView = loginView;
    }

    public void setPresenter(LoginContract.Presenter presenter) {
        LoginContract.Presenter mLoginPresenter = presenter;
    }

    public void loginWithInstagram() {
        Context mContext = mLoginView.getActivity().getApplicationContext();
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(mContext.getString(R.string.insta_https))
                .authority(mContext.getString(R.string.insta_url))
                .appendPath(mContext.getString(R.string.insta_oauth))
                .appendPath(mContext.getString(R.string.insta_authorize))
                .appendQueryParameter(mContext.getString(R.string.insta_client_id), WebConstants.INSTA_CLIENT_ID)
                .appendQueryParameter(mContext.getString(R.string.insta_redirect_uri), WebConstants.INSTA_CALLBACK_URL)
                .appendQueryParameter(mContext.getString(R.string.insta_response_type), mContext.getString(R.string.insta_token));
        SnapXToast.showToast(mContext, mContext.getString(R.string.insta_login_success));
    }
}



