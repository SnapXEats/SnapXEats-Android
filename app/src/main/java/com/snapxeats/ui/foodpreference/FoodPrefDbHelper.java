package com.snapxeats.ui.foodpreference;

import android.content.Context;
import android.content.SharedPreferences;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import com.snapxeats.common.model.preference.UserFoodPreferencesDao;
import com.snapxeats.common.utilities.AppUtility;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

@Singleton
public class FoodPrefDbHelper {
    private Context mContext;

    @Inject
    AppUtility utility;

    @Inject
    DbHelper dbHelper;

    @Inject
    public FoodPrefDbHelper() {
    }

    private UserFoodPreferencesDao foodPreferencesDao;

    public void setContext(Context context) {
        this.mContext = context;
        utility.setContext(mContext);
        dbHelper.setContext(mContext);
    }

    UserFoodPreferencesDao getFoodPreferencesDao() {
        foodPreferencesDao = dbHelper.getUserFoodPreferencesDao();
        return foodPreferencesDao;
    }

    List<FoodPref> getFoodPrefData(List<FoodPref> rootFoodPrefList,
                                   List<UserFoodPreferences> userFoodPreferencesList) {
        if (null != userFoodPreferencesList && ZERO < userFoodPreferencesList.size()) {
            for (int index = ZERO; index < userFoodPreferencesList.size(); index++) {
                for (int rootIndex = ZERO; rootIndex < rootFoodPrefList.size(); rootIndex++) {
                    if (userFoodPreferencesList.get(index).getFood_type_info_id().equalsIgnoreCase
                            (rootFoodPrefList.get(rootIndex).getFood_type_info_id())) {
                        rootFoodPrefList.get(rootIndex).set_food_favourite(userFoodPreferencesList.get(index).getIs_food_favourite());
                        rootFoodPrefList.get(rootIndex).set_food_like(userFoodPreferencesList.get(index).getIs_food_like());
                        rootFoodPrefList.get(rootIndex).setSelected(true);
                    }
                }
            }
        }
        return rootFoodPrefList;
    }


    /**
     * To manage selected food preferences count
     */
    List<FoodPref> getSelectedFoodList(List<FoodPref> foodPrefList) {

        List<FoodPref> selectedFoodList = new ArrayList<>();
        if (foodPrefList != null) {
            for (FoodPref foodPref : foodPrefList) {
                if (foodPref.is_food_like() || foodPref.is_food_favourite()) {
                    selectedFoodList.add(foodPref);
                }
            }
        }
        return selectedFoodList;
    }

    List<UserFoodPreferences> getSelectedUserFoodPreferencesList
            (List<FoodPref> foodPrefList) {

        List<UserFoodPreferences> selectedFoodList = new ArrayList<>();
        UserFoodPreferences userCuisinePreferences;
        if (foodPrefList != null) {
            for (FoodPref foodPref : foodPrefList) {
                if (foodPref.isSelected()) {
                    userCuisinePreferences = new UserFoodPreferences(foodPref.getFood_type_info_id()
                            , foodPref.is_food_like(), foodPref.is_food_favourite(), "");
                    selectedFoodList.add(userCuisinePreferences);
                }
            }
        }
        return selectedFoodList;
    }

    void saveFoodPrefList(List<FoodPref> foodPrefList) {
        foodPreferencesDao.deleteAll();
        UserFoodPreferences foodPreferences = null;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (FoodPref foodPref : foodPrefList) {
            if (foodPref.is_food_like() || foodPref.is_food_favourite()) {
                foodPreferences = new UserFoodPreferences(foodPref.getFood_type_info_id(),
                        foodPref.is_food_like(), foodPref.is_food_favourite(), userId);
                foodPreferencesDao.insert(foodPreferences);
            }
        }
    }
}
