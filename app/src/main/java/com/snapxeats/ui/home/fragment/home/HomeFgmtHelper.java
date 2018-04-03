package com.snapxeats.ui.home.fragment.home;

import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserFoodPreferences;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 14/3/18.
 */

@Singleton
public class HomeFgmtHelper {

    private List<String> cuisineList;
    private List<String> foodList;

    @Inject
    public HomeFgmtHelper() {
    }

    @Inject
    RootUserPreference rootUserPreference;

    SelectedCuisineList getSelectedCusine(LocationCuisine mLocationCuisine, List<String> selectedList) {
        SelectedCuisineList selectedCuisineList = new SelectedCuisineList();
        selectedCuisineList.setLocation(mLocationCuisine);
        cuisineList = new ArrayList<>();
        foodList = new ArrayList<>();

        if (null != rootUserPreference) {
            cuisineList = getCuisineList(selectedList);
            foodList = getFoodList();


            if (null != cuisineList) {
                selectedCuisineList.setSelectedCuisineList(cuisineList);
            }
            if (null != foodList) {
                selectedCuisineList.setSelectedFoodList(foodList);
            }

            if (null != rootUserPreference.getRestaurant_distance() &&
                    !rootUserPreference.getRestaurant_distance().isEmpty()) {
                selectedCuisineList.setRestaurant_distance
                        (Integer.parseInt(rootUserPreference.getRestaurant_distance()));
            } else {
                selectedCuisineList.setRestaurant_distance(1);
            }

            if (null != rootUserPreference.getRestaurant_price() &&
                    !rootUserPreference.getRestaurant_price().isEmpty()) {
                selectedCuisineList.setRestaurant_price(Integer.parseInt(rootUserPreference.getRestaurant_price()));
            } else {
                selectedCuisineList.setRestaurant_price(0);
            }

            if (null != rootUserPreference.getRestaurant_rating() &&
                    !rootUserPreference.getRestaurant_rating().isEmpty()) {
                selectedCuisineList.setRestaurant_rating(Integer.parseInt(rootUserPreference.getRestaurant_rating()));
            } else {
                selectedCuisineList.setRestaurant_rating(0);
            }


            if (rootUserPreference.isSort_by_distance()) {
                selectedCuisineList.setSort_by_distance(1);
            } else if (rootUserPreference.isSort_by_rating()) {
                selectedCuisineList.setSort_by_rating(1);
            } else {
                selectedCuisineList.setSort_by_distance(1);
            }
        }
        return selectedCuisineList;
    }

    public List<String> getCuisineList(List<String> selectedList) {

        if (null != rootUserPreference.getUserCuisinePreferences()) {
            for (UserCuisinePreferences cuisinePreferences : rootUserPreference.getUserCuisinePreferences()) {
                cuisineList.add(cuisinePreferences.getCuisine_info_id());
            }
        } else {
            cuisineList.addAll(selectedList);
        }
        return cuisineList;
    }

    public List<String> getFoodList() {
        if (null != rootUserPreference.getUserFoodPreferences()) {

            for (UserFoodPreferences foodPreferences : rootUserPreference.getUserFoodPreferences()) {
                foodList.add(foodPreferences.getFood_type_info_id());
            }
        }
        return foodList;
    }
}
