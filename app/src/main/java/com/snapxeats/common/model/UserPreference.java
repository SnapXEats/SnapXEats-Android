package com.snapxeats.common.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Snehal Tembare on 8/2/18.
 */


@Entity
public class UserPreference {

    private String user_id;
    private String user_preferences_id;
    private String restaurant_rating;
    private String restaurant_price;
    private String restaurant_distance;
    private boolean sort_by_distance;
    private boolean sort_by_rating;
//    private List<UserCuisinePreferences> user_cuisine_preferences;
//    private List<UserFoodPreferences> user_food_preferences;
    @Generated(hash = 2037384390)
    public UserPreference(String user_id, String user_preferences_id,
            String restaurant_rating, String restaurant_price, String restaurant_distance,
            boolean sort_by_distance, boolean sort_by_rating) {
        this.user_id = user_id;
        this.user_preferences_id = user_preferences_id;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_price = restaurant_price;
        this.restaurant_distance = restaurant_distance;
        this.sort_by_distance = sort_by_distance;
        this.sort_by_rating = sort_by_rating;
    }
    @Generated(hash = 1390964)
    public UserPreference() {
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_preferences_id() {
        return this.user_preferences_id;
    }
    public void setUser_preferences_id(String user_preferences_id) {
        this.user_preferences_id = user_preferences_id;
    }
    public String getRestaurant_rating() {
        return this.restaurant_rating;
    }
    public void setRestaurant_rating(String restaurant_rating) {
        this.restaurant_rating = restaurant_rating;
    }
    public String getRestaurant_price() {
        return this.restaurant_price;
    }
    public void setRestaurant_price(String restaurant_price) {
        this.restaurant_price = restaurant_price;
    }
    public String getRestaurant_distance() {
        return this.restaurant_distance;
    }
    public void setRestaurant_distance(String restaurant_distance) {
        this.restaurant_distance = restaurant_distance;
    }
    public boolean getSort_by_distance() {
        return this.sort_by_distance;
    }
    public void setSort_by_distance(boolean sort_by_distance) {
        this.sort_by_distance = sort_by_distance;
    }
    public boolean getSort_by_rating() {
        return this.sort_by_rating;
    }
    public void setSort_by_rating(boolean sort_by_rating) {
        this.sort_by_rating = sort_by_rating;
    }

}
