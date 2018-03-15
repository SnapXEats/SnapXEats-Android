package com.snapxeats.ui.foodpreference;

import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

@Singleton
public class FoodPrefDbHelper {

    @Inject
    public FoodPrefDbHelper() {
    }

    public List<FoodPref> getFoodPrefData(List<FoodPref> rootFoodPrefList,
                                          List<UserFoodPreferences> userFoodPreferencesList) {
        if (null != userFoodPreferencesList && 0 < userFoodPreferencesList.size()) {
            for (int index = 0; index < userFoodPreferencesList.size(); index++) {
                for (int rootIndex = 0; rootIndex < rootFoodPrefList.size(); rootIndex++) {
                    if (userFoodPreferencesList.get(index).getFood_type_info_id().equalsIgnoreCase
                            (rootFoodPrefList.get(rootIndex).getFood_type_info_id())) {
                        rootFoodPrefList.get(rootIndex).set_food_favourite(userFoodPreferencesList.get(index).getIs_food_favourite());
                        rootFoodPrefList.get(rootIndex).set_food_like(userFoodPreferencesList.get(index).getIs_food_like());
                    }
                }
            }
        }
        return rootFoodPrefList;
    }


    /**
     * To manage selected food preferences count
     */
    public List<FoodPref> getSelectedFoodList(List<FoodPref> foodPrefList) {

        List<FoodPref> selectedFoodList = new ArrayList<>();
        if (foodPrefList != null) {
            for (FoodPref foodPref : foodPrefList) {
                if (foodPref.is_food_like() || foodPref.is_food_favourite()) {
                    selectedFoodList.add(foodPref);
                }
            }
        }
        return selectedFoodList != null ? selectedFoodList : null;
    }

    public List<UserFoodPreferences> getSelectedUserFoodPreferencesList
            (List<FoodPref> foodPrefList) {

        List<UserFoodPreferences> selectedFoodList = new ArrayList<>();
        UserFoodPreferences userCuisinePreferences;
        if (foodPrefList != null) {
            for (FoodPref foodPref : foodPrefList) {
                if (foodPref.is_food_like() || foodPref.is_food_favourite()) {
                    userCuisinePreferences = new UserFoodPreferences(foodPref.getFood_type_info_id()
                            , foodPref.is_food_like(), foodPref.is_food_favourite(), "");
                    selectedFoodList.add(userCuisinePreferences);
                }
            }
        }
        return selectedFoodList != null ? selectedFoodList : null;
    }
}
