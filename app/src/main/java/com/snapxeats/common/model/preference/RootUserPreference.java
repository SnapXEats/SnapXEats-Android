package com.snapxeats.common.model.preference;

import com.snapxeats.common.model.location.Location;

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
    private Location location;
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

    public void resetRootUserPreference() {
        setUser_Id("");
        setLocation(null);
        setRestaurant_rating("");
        setRestaurant_price("");
        setRestaurant_distance("");
        setSort_by_rating(false);
        setSort_by_distance(false);
        setUserCuisinePreferences(null);
        setUserFoodPreferences(null);
    }
}