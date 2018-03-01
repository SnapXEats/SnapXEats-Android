package com.snapxeats.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.RootInstagram;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.ui.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 11/1/18.
 */

public class InstagramApp {

    private InstagramDialog instagramDialog;
    private Context context;
    public static String mCallbackUrl = "";
    private RootInstagram rootInstagram;

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
                getInstaInfo(code);
            }

            @Override
            public void onError(String error) {

            }
        };

        instagramDialog = new InstagramDialog(context, mAuthUrl, listener);
        ProgressDialog mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    //get instagram info
    public void getInstaInfo(String token) {
        ApiHelper apiHelper = ApiClient.getClient(context, BASE_URL).create(ApiHelper.class);
        Call<RootInstagram> snapXUserCall = apiHelper.getInstagramInfo(token);
        snapXUserCall.enqueue(new Callback<RootInstagram>() {
            @Override
            public void onResponse(Call<RootInstagram> call, Response<RootInstagram> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rootInstagram = response.body();
                    saveDataInDb(token, rootInstagram);

                    SnapXToast.showToast(InstagramApp.this.context, context.getString(R.string.insta_login_success));
                    Intent intent = new Intent(context, HomeActivity.class);
                    rootInstagram.setInstagramToken(token);
                    //Save data in db
                    intent.putExtra(context.getString(R.string.instaInfoIntent), rootInstagram);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RootInstagram> call, Throwable t) {
            }
        });
    }

    private void saveDataInDb(String token, RootInstagram rootInstagram) {

        DaoSession daoSession = ((SnapXApplication) context.getApplicationContext()).getDaoSession();
        SnapxDataDao snapxDataDao = daoSession.getSnapxDataDao();

        SnapxData snapxData = new SnapxData();

        /*snapxData.setSocialToken(token);
        snapxData.setSocialUserId(rootInstagram.getData().getId());
        snapxData.setUserName(rootInstagram.getData().getFull_name());
        snapxData.setUserImage(rootInstagram.getData().getProfile_picture());*/

        snapxData.setSocialToken("6898929419.739cbd7.8e2b496c9a524c68be878ba51f7bd044");
        snapxData.setSocialUserId("6898929419");
        snapxData.setUserName("snapxeats");
        snapxData.setUserImage("https://scontent.cdninstagram.com/vp/430b0b2120cf968dda35b6d342f99a3b/5B18595E/t51.2885-19/s150x150/27892527_245377196003325_939776446803476480_n.jpg");

        snapxDataDao.insert(snapxData);
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
