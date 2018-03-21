package com.snapxeats.ui.foodpreference;

import android.content.Context;

import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.common.model.preference.RootFoodPref;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
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
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPrefInteractor {
    private FoodPreferenceContract.FoodPreferencePresenter presenter;
    private FoodPreferenceContract.FoodPreferenceView view;
    private Context mContext;
    private UserFoodPreferencesDao foodPreferencesDao;

    @Inject
    public FoodPrefInteractor() {
    }

    @Inject
    AppUtility utility;

    @Inject
    FoodPrefDbHelper helper;

    public void setPresenter(FoodPreferenceContract.FoodPreferencePresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(FoodPreferenceContract.FoodPreferenceView view) {
        this.view = view;
        this.mContext = view.getActivity();
        utility.setContext(mContext);
        helper.setContext(mContext);
        foodPreferencesDao = helper.getFoodPreferencesDao();
    }

    void getFoodPrefList() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodPref> foodPrefCall = apiHelper.getFoodPreferences();
            foodPrefCall.enqueue(new Callback<RootFoodPref>() {
                @Override
                public void onResponse(Call<RootFoodPref> call, Response<RootFoodPref> response) {
                    if (response.isSuccessful() && null != response.body().getFoodTypeList()) {
                        presenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<RootFoodPref> call, Throwable t) {
                    presenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            presenter.response(SnapXResult.NONETWORK, null);
        }
    }

    void saveFoodPrefList(List<FoodPref> foodPrefList) {
        helper.saveFoodPrefList(foodPrefList);
    }
}
