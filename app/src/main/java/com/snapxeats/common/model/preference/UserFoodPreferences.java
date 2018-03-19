package com.snapxeats.common.model.preference;

import org.greenrobot.greendao.annotation.Entity;import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Snehal Tembare on 11/2/18.
 */

@Entity
public class UserFoodPreferences {
    @Id(autoincrement = true)
    private transient Long id;

    private String food_type_info_id;
    private boolean is_food_like;
    private boolean is_food_favourite;
    private String userPreferenceId;
    @Generated(hash = 13039529)
    public UserFoodPreferences(String food_type_info_id, boolean is_food_like,
            boolean is_food_favourite, String userPreferenceId) {
        this.food_type_info_id = food_type_info_id;
        this.is_food_like = is_food_like;
        this.is_food_favourite = is_food_favourite;
        this.userPreferenceId = userPreferenceId;
    }
    @Generated(hash = 53000869)
    public UserFoodPreferences() {
    }
    public String getFood_type_info_id() {
        return this.food_type_info_id;
    }
    public void setFood_type_info_id(String food_type_info_id) {
        this.food_type_info_id = food_type_info_id;
    }
    public boolean getIs_food_like() {
        return this.is_food_like;
    }
    public void setIs_food_like(boolean is_food_like) {
        this.is_food_like = is_food_like;
    }
    public boolean getIs_food_favourite() {
        return this.is_food_favourite;
    }
    public void setIs_food_favourite(boolean is_food_favourite) {
        this.is_food_favourite = is_food_favourite;
    }
    public String getUserPreferenceId() {
        return this.userPreferenceId;
    }
    public void setUserPreferenceId(String userPreferenceId) {
        this.userPreferenceId = userPreferenceId;
    }
}
