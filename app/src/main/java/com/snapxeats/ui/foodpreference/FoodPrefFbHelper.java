package com.snapxeats.ui.foodpreference;

import com.snapxeats.common.model.FoodPref;
import com.snapxeats.common.model.UserFoodPreferences;

import java.util.List;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class FoodPrefFbHelper {

    public void getFoodPrefData(List<FoodPref> rootFoodPrefList, List<UserFoodPreferences> userFoodPreferencesList) {
        if (null != userFoodPreferencesList && 0 < userFoodPreferencesList.size()) {
            for (int index = 0; index < rootFoodPrefList.size(); index++) {
                if (userFoodPreferencesList.get(index).getIs_food_favourite()) {
                    rootFoodPrefList.get(index).set_food_favourite
                            (userFoodPreferencesList.get(index).getIs_food_favourite());
                } else if (userFoodPreferencesList.get(index).getIs_food_like()) {
                    rootFoodPrefList.get(index).set_food_like
                            (userFoodPreferencesList.get(index).getIs_food_like());
                }
            }
        }
    }
}
