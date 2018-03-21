package com.snapxeats.ui.foodpreference;

import android.app.Activity;
import android.content.SharedPreferences;
import com.snapxeats.R;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
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
    private Activity mContext;
    private DaoSession daoSession;
    private UserFoodPreferencesDao foodPreferencesDao;

    @Inject
    public FoodPrefInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setPresenter(FoodPreferenceContract.FoodPreferencePresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(FoodPreferenceContract.FoodPreferenceView view) {
        this.view = view;
        this.mContext = view.getActivity();
        utility.setContext(mContext);
        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
        foodPreferencesDao = daoSession.getUserFoodPreferencesDao();
    }

    public void getFoodPrefList() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodPref> foodPrefCall = apiHelper.getFoodPreferences();
            foodPrefCall.enqueue(new Callback<RootFoodPref>() {
                @Override
                public void onResponse(Call<RootFoodPref> call, Response<RootFoodPref> response) {
                    if (response.isSuccessful() && response.body().getFoodTypeList() != null) {
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

    public void saveFoodPrefList(List<FoodPref> foodPrefList) {

        foodPreferencesDao.deleteAll();
        UserFoodPreferences foodPreferences = null;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (FoodPref foodPref : foodPrefList) {
            if (foodPref.is_food_like() || foodPref.is_food_favourite()) {
                foodPreferences = new UserFoodPreferences(foodPref.getFood_type_info_id(),
                        foodPref.is_food_like(), foodPref.is_food_favourite(), userId);

                if (null != foodPreferences) {
                    foodPreferencesDao.insert(foodPreferences);
                }
            }
        }
    }
}
