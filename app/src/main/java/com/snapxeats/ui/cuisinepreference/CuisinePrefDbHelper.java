package com.snapxeats.ui.cuisinepreference;

import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

@Singleton
public class CuisinePrefDbHelper {

    @Inject
    public CuisinePrefDbHelper() {
    }

    /**
     * Map Selected Cuisines to cuisine list
     */
    List<Cuisines> getCuisinePrefData(List<UserCuisinePreferences> cuisinedPrefList,
                                      List<Cuisines> rootCuisineList) {

        if (null != cuisinedPrefList && 0 < cuisinedPrefList.size()) {
            for (int index = 0; index < cuisinedPrefList.size(); index++) {
                for (int rootIndex = 0; rootIndex < rootCuisineList.size(); rootIndex++) {
                    if (cuisinedPrefList.get(index).getCuisine_info_id().equalsIgnoreCase
                            (rootCuisineList.get(rootIndex).getCuisine_info_id())) {
                        rootCuisineList.get(rootIndex).set_cuisine_favourite(cuisinedPrefList
                                .get(index).getIs_cuisine_favourite());
                        rootCuisineList.get(rootIndex).set_cuisine_like(cuisinedPrefList
                                .get(index).getIs_cuisine_like());
                    }
                }
            }
        }
        return rootCuisineList;
    }

    /**
     * To manage selected cuisine count
     */
    public List<Cuisines> getSelectedCuisineList(List<Cuisines> rootCuisineList) {

        List<Cuisines> selectedCuisineList = new ArrayList<>();
        if (null!=rootCuisineList) {
            for (Cuisines cuisines : rootCuisineList) {
                if (cuisines.is_cuisine_favourite() || cuisines.is_cuisine_like()) {
                    selectedCuisineList.add(cuisines);
                }
            }
        }
        return selectedCuisineList;
    }

    public List<UserCuisinePreferences> getSelectedUserCuisinePreferencesList
            (List<Cuisines> rootCuisineList) {

        List<UserCuisinePreferences> selectedCuisineList = new ArrayList<>();
        UserCuisinePreferences userCuisinePreferences;
        if (null!=rootCuisineList) {
            for (Cuisines cuisines : rootCuisineList) {
                if (cuisines.is_cuisine_favourite() || cuisines.is_cuisine_like()) {
                    userCuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id()
                            , cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite(), "");
                    selectedCuisineList.add(userCuisinePreferences);
                }
            }
        }
        return selectedCuisineList;
    }
}
