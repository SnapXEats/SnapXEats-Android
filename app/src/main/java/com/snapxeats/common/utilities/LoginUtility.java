package com.snapxeats.common.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.SnapXUser;
import com.snapxeats.common.model.login.RootInstagram;
import com.snapxeats.common.model.preference.SnapXPreference;
import com.snapxeats.common.model.preference.UserPreferences;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.ui.home.fragment.wishlist.WishlistDbHelper;
import com.snapxeats.ui.login.LoginDbHelper;

import net.hockeyapp.android.utils.Base64;

import java.security.MessageDigest;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.UIConstants.FB_SHA;
import static com.snapxeats.common.constants.UIConstants.PROFILE_WIDTH_HEIGHT;
import static com.snapxeats.common.constants.UIConstants.SX_BEARER;
import static com.snapxeats.common.constants.UIConstants.ZERO;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 21/5/18.
 */
@Singleton
public class LoginUtility {

    @Inject
    LoginUtility() {
    }

    @Inject
    SnapXDialog snapXDialog;

    private Context mContext;
    private SnapXData snapXData;
    private SnapXDataDao snapxDataDao;

    @Inject
    public AppUtility appUtility;

    @Inject
    DbHelper dbHelper;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    LoginDbHelper loginDbHelper;

    public void setContext(Context context) {
        this.mContext = context;
        snapXDialog.setContext((Activity) context);
        dbHelper.setContext(context);
        loginDbHelper.setContext(context);
        wishlistDbHelper.setContext(context);
        snapxDataDao = dbHelper.getSnapxDataDao();
        snapXData = new SnapXData();
    }

    /**
     * TODO- Relogin user
     * GET- Get user preferences
     */
    public void getUserPreferences(String token) {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<SnapXPreference> userPreferenceCall = apiHelper.getUserPreferences(SX_BEARER + token);

            userPreferenceCall.enqueue(new Callback<SnapXPreference>() {
                @Override
                public void onResponse(Call<SnapXPreference> call, Response<SnapXPreference> response) {
                    if (response.isSuccessful() && null != response.body())
                        savePreferenceDataInDb(response.body().getUserPreferences());
                }

                @Override
                public void onFailure(Call<SnapXPreference> call, Throwable t) {
                }
            });
        }
    }

    public void savePreferenceDataInDb(UserPreferences rootUserPreference) {
        loginDbHelper.saveUserPrefDataInDb(rootUserPreference);
    }

    //save data to db
    public void saveInstaDataInDb(SnapXUser snapXUser, String token, RootInstagram rootInstagram) {
        //User data from server
        saveServerDataInDb(snapXUser, rootInstagram);
        saveWishlistDataInDb(snapXUser);
        snapXData.setSocialToken(token);
        snapXData.setSocialUserId(rootInstagram.getData().getId());
        snapXData.setUserName(rootInstagram.getData().getFull_name());
        snapXData.setImageUrl(rootInstagram.getData().getProfile_picture());
        if (ZERO == snapxDataDao.loadAll().size()) {
            snapxDataDao.insert(snapXData);
        } else {
            snapxDataDao.update(snapXData);
        }
    }

    public void saveServerDataInDb(SnapXUser snapXUser, RootInstagram rootInstagram) {
        snapXData.setUserId(snapXUser.getUser_id());
        snapXData.setToken(snapXUser.getToken());
        snapXData.setSocialPlatform(snapXUser.getSocial_platform());
        snapXData.setIsFirstTimeUser(snapXUser.isFirst_time_login());
        if (snapXUser.getSocial_platform().equalsIgnoreCase(mContext.getString(R.string.platform_facebook))) {
            if (null != Profile.getCurrentProfile()) {
                String userName = Profile.getCurrentProfile().getFirstName() + " "
                        + Profile.getCurrentProfile().getLastName();
                Uri profileUri = Profile.getCurrentProfile().getProfilePictureUri(PROFILE_WIDTH_HEIGHT,
                        PROFILE_WIDTH_HEIGHT);
                snapXData.setUserName(userName);
                snapXData.setImageUrl(profileUri.toString());
            }

        } else if (snapXUser.getSocial_platform().equalsIgnoreCase(mContext.getString(R.string.platform_instagram))) {
            snapXData.setImageUrl(rootInstagram.getData().getProfile_picture());
            snapXData.setUserName(rootInstagram.getData().getFull_name());
        }
    }

    public void saveFbDataInDb(SnapXUser snapXUser, RootInstagram rootInstagram) {
        saveServerDataInDb(snapXUser, rootInstagram);
        saveWishlistDataInDb(snapXUser);
        snapXData.setUserId(AccessToken.getCurrentAccessToken().getUserId());
        snapXData.setSocialToken(AccessToken.getCurrentAccessToken().getToken());
        if (ZERO == snapxDataDao.loadAll().size()) {
            snapxDataDao.insert(snapXData);
        } else {
            snapxDataDao.update(snapXData);
        }
    }

    public void saveWishlistDataInDb(SnapXUser snapXUser) {
        if (null != snapXUser.getUserWishList() &&
                ZERO != snapXUser.getUserWishList().size()) {
            wishlistDbHelper.saveWishlistDataInDb(snapXUser.getUserWishList());
        }
    }

    /**
     * get facebook hash key for exachanging information between app and Facebook
     **/
    @SuppressLint("PackageManagerGetSignatures")
    public void getFbHashKey(Activity context) {
        PackageInfo packageInfo;
        try {
            String packageName = mContext.getApplicationContext().getPackageName();
            packageInfo = mContext.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {

                MessageDigest md = MessageDigest.getInstance(FB_SHA);
                md.update(signature.toByteArray());
                //hashkey for reference only
                String key = new String(Base64.encode(md.digest(), ZERO));
                SnapXToast.debug(context.getString(R.string.fb_hashkey) + key);
            }
        } catch (Exception ignored) {
        }
    }
}
