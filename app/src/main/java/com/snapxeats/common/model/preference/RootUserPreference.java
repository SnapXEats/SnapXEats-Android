package com.snapxeats.common.model.preference;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 7/3/18.
 */

@Getter
@Setter

@Singleton
public class RootUserPreference{
    private String user_Id;
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
    private List<UserCuisinePreferences> userCuisinePreferences;
    private List<UserFoodPreferences> userFoodPreferences;

    @Inject
    public RootUserPreference() {
    }
}