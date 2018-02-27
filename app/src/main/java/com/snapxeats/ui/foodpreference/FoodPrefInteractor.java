package com.snapxeats.ui.foodpreference;

import android.app.Activity;

import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.FoodPref;
import com.snapxeats.common.model.RootFoodPref;
import com.snapxeats.common.model.UserCuisinePreferences;
import com.snapxeats.common.model.UserCuisinePreferencesDao;
import com.snapxeats.common.model.UserFoodPreferences;
import com.snapxeats.common.model.UserFoodPreferencesDao;
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

    public void setPresenter(FoodPreferenceContract.FoodPreferencePresenter presenter) {
        this.presenter = presenter;
    }

    public void setContext(FoodPreferenceContract.FoodPreferenceView view) {
        this.view = view;
        this.mContext = view.getActivity();
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
        for (FoodPref foodPref : foodPrefList) {
            foodPreferences = new UserFoodPreferences("",foodPref.getFood_type_info_id(),
                    foodPref.is_food_like(), foodPref.is_food_favourite());

            if (null != foodPreferences) {
                foodPreferencesDao.insert(foodPreferences);
            }
        }
    }


    public List<UserFoodPreferences> getFoodPrefListFromDb() {

        return foodPreferencesDao.loadAll() != null ? foodPreferencesDao.loadAll() : null;
    }
}
