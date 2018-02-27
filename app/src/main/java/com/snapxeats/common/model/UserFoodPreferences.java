package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;

import lombok.Getter;
import lombok.Setter;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Snehal Tembare on 11/2/18.
 */

@Entity
public class UserFoodPreferences {
    private String user_id;
    private String food_type_info_id;
    private boolean is_food_like;
    private boolean is_food_favourite;

    @Generated(hash = 403589711)
    public UserFoodPreferences(String user_id,
                               String food_type_info_id,
                               boolean is_food_like,
                               boolean is_food_favourite) {
        this.user_id = user_id;
        this.food_type_info_id = food_type_info_id;
        this.is_food_like = is_food_like;
        this.is_food_favourite = is_food_favourite;
    }

    @Generated(hash = 53000869)
    public UserFoodPreferences() {
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
}
