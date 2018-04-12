package com.snapxeats.common.model.preference;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 4/4/18.
 */
@Getter
@Setter
public class UserPreferences {
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
    private List<UserCuisinePreferences> userCuisinePreferences;
    private List<UserFoodPreferences> userFoodPreferences;
}