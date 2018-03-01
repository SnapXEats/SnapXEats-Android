package com.snapxeats.ui.cuisinepreference;

import android.app.Activity;

import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.UserCuisinePreferences;

import java.util.List;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

public class CuisinePrefDbHelper {

    public List<Cuisines> getCuisinePrefData(List<UserCuisinePreferences> cuisinedPrefList,
                                             List<Cuisines> rootCuisineList) {


        if (null != cuisinedPrefList && 0 < cuisinedPrefList.size()) {
            for (int index = 0; index < rootCuisineList.size(); index++) {
                if (cuisinedPrefList.get(index).getIs_cuisine_favourite()) {
                    rootCuisineList.get(index).set_cuisine_favourite
                            (cuisinedPrefList.get(index).getIs_cuisine_favourite());
                } else if (cuisinedPrefList.get(index).getIs_cuisine_like()) {
                    rootCuisineList.get(index).set_cuisine_like
                            (cuisinedPrefList.get(index).getIs_cuisine_like());
                }
            }
        }
        return rootCuisineList;
    }
}
