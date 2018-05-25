package com.snapxeats.ui.cuisinepreference;

import android.content.Context;
import android.content.SharedPreferences;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.common.model.preference.UserCuisinePreferences;
import com.snapxeats.common.model.preference.UserCuisinePreferencesDao;
import com.snapxeats.common.utilities.AppUtility;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Snehal Tembare on 28/2/18.
 */

@Singleton
public class CuisinePrefDbHelper {

    private Context mContext;

    @Inject
    CuisinePrefDbHelper() {
    }


    @Inject
    AppUtility utility;

    @Inject
    DbHelper dbHelper;

    public void setContext(Context context) {
        this.mContext = context;
        utility.setContext(mContext);
        dbHelper.setContext(mContext);
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
                        rootCuisineList.get(rootIndex).setSelected(true);
                    }
                }
            }
        }
        return rootCuisineList;
    }

    /**
     * To manage selected cuisine count
     */
    List<Cuisines> getSelectedCuisineList(List<Cuisines> rootCuisineList) {

        List<Cuisines> selectedCuisineList = new ArrayList<>();
        if (null != rootCuisineList) {
            for (Cuisines cuisines : rootCuisineList) {
                if (cuisines.is_cuisine_favourite() || cuisines.is_cuisine_like()) {
                    selectedCuisineList.add(cuisines);
                }
            }
        }
        return selectedCuisineList;
    }

    List<UserCuisinePreferences> getSelectedUserCuisinePreferencesList
            (List<Cuisines> rootCuisineList) {

        List<UserCuisinePreferences> selectedCuisineList = new ArrayList<>();
        UserCuisinePreferences userCuisinePreferences;
        if (null != rootCuisineList) {
            for (Cuisines cuisines : rootCuisineList) {
                if (cuisines.isSelected() && cuisines.is_cuisine_like() || cuisines.is_cuisine_favourite()) {
                    userCuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id()
                            , cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite(), "");
                    selectedCuisineList.add(userCuisinePreferences);
                }
            }
        }
        return selectedCuisineList;
    }

    void saveCuisineList(List<Cuisines> rootCuisineList) {

        UserCuisinePreferencesDao cuisinePreferencesDao = dbHelper.getUserCuisinePreferencesDao();
        cuisinePreferencesDao.deleteAll();
        UserCuisinePreferences cuisinePreferences = null;
        SharedPreferences preferences = utility.getSharedPreferences();
        String userId = preferences.getString(mContext.getString(R.string.user_id), "");

        for (Cuisines cuisines : rootCuisineList) {
            if (cuisines.is_cuisine_like() || cuisines.is_cuisine_favourite()) {
                cuisinePreferences = new UserCuisinePreferences(cuisines.getCuisine_info_id(),
                        cuisines.is_cuisine_like(), cuisines.is_cuisine_favourite(), userId);
                cuisinePreferencesDao.insert(cuisinePreferences);
            }
        }
    }
}
