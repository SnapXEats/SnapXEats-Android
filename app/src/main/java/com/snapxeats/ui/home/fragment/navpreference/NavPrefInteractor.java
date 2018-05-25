package com.snapxeats.ui.home.fragment.navpreference;

import android.content.Context;

import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.SnapXData;
import com.snapxeats.common.model.SnapXDataDao;
import com.snapxeats.common.model.preference.RootUserPreference;
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

    public void applyPreferences(UserPreference userPreference) {
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

    void saveUserData() {
        SnapXDataDao snapxDataDao = dbHelper.getSnapxDataDao();
        if (snapxDataDao.loadAll().size() > 0) {
            List<SnapXData> snapXDataList = snapxDataDao.loadAll();
            snapXDataList.get(0).setIsFirstTimeUser(false);
            snapxDataDao.update(snapXDataList.get(0));
        }
    }
}
