package com.snapxeats.ui.home;

import android.content.Context;
import com.snapxeats.common.model.SnapxData;
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
 * Created by Snehal Tembare on 28/2/18.
 */

public class HomeInteractor {
    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    HomeDbHelper homeDbHelper;

    @Inject
    RootUserPreference rootUserPreference;

    @Inject
    public HomeInteractor() {
    }

    private HomeContract.HomePresenter homePresenter;

    public void setContext(HomeContract.HomeView context) {
        this.mContext = context.getActivity();
        utility.setContext(context.getActivity());
        homeDbHelper.setContext(mContext);
    }

    public void setHomePresenter(HomeContract.HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public List<SnapxData> getUserInfoFromDb() {
        return homeDbHelper.getUserInfoFromDb();
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
                        homePresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void saveDataInLocalDb(UserPreference userPreference) {
        homeDbHelper.saveDataInLocalDb(userPreference);
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
                        homePresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<UserPreference> call, Throwable t) {
                    homePresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homePresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public RootUserPreference getUserPreferenceFromDb() {
        return homeDbHelper.getUserPreferenceFromDb();
    }
}
