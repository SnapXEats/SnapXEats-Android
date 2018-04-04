package com.snapxeats.ui.home.fragment.navpreference;

import android.content.Context;
import android.content.SharedPreferences;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 9/2/18.
 */

public class NavPrefInteractor {

    private NavPrefContract.NavPrefPresenter navPrefPresenter;
    private NavPrefContract.NavPrefView view;
    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    RootUserPreference rootUserPreference;

    @Inject
    PrefDbHelper helper;

    @Inject
    DbHelper dbHelper;

    @Inject
    public NavPrefInteractor() {
    }

    public void setPreferencePresenter(NavPrefContract.NavPrefPresenter presenter) {
        this.navPrefPresenter = presenter;
    }

    public void setContext(NavPrefContract.NavPrefView context) {
        this.view = context;
        mContext = context.getActivity();
        utility.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    /**
     * POST- Save user preferences
     */

    public void applyPreferences(UserPreference userPreference)     {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<UserPreference> userPreferenceCall = apiHelper.setUserPreferences(utility.getAuthToken(mContext), userPreference);
            userPreferenceCall.enqueue(new Callback<UserPreference>() {
                @Override
                public void onResponse(Call<UserPreference> call, Response<UserPreference> response) {
                    if (response.isSuccessful()) {
                        navPrefPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    navPrefPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            navPrefPresenter.response(SnapXResult.NONETWORK, null);
        }
    }


    public void saveDataInLocalDb(UserPreference userPreference) {
        helper.userPreference(userPreference);
    }

    /**
     * PUT- Update user preferences
     */
    public void updatePreferences(UserPreference userPreference) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<UserPreference> userPreferenceCall = apiHelper.updateUserPreferences(utility.getAuthToken(mContext), userPreference);
            userPreferenceCall.enqueue(new Callback<UserPreference>() {
                @Override
                public void onResponse(Call<UserPreference> call, Response<UserPreference> response) {
                    if (response.isSuccessful()) {
                        navPrefPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    navPrefPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            navPrefPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    /**
     * TODO- Relogin user
     * GET- Get user preferences
     */
    public void getUserPreferences() {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            SharedPreferences preferences = utility.getSharedPreferences();

            Call<RootUserPreference> userPreferenceCall = apiHelper.getUserPreferences(utility.getAuthToken(mContext));

            userPreferenceCall.enqueue(new Callback<RootUserPreference>() {
                @Override
                public void onResponse(Call<RootUserPreference> call, Response<RootUserPreference> response) {
                    if (response.isSuccessful() && null != response.body())
                        navPrefPresenter.response(SnapXResult.SUCCESS, response.body());
                }

                @Override
                public void onFailure(Call<RootUserPreference> call, Throwable t) {
                    navPrefPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            navPrefPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    void saveUserData() {
        SnapxDataDao snapxDataDao = dbHelper.getSnapxDataDao();
        if (snapxDataDao.loadAll().size() > 0) {
            List<SnapxData> snapxDataList = snapxDataDao.loadAll();
            snapxDataList.get(0).setIsFirstTimeUser(false);
            snapxDataDao.update(snapxDataList.get(0));
        }
    }
}
