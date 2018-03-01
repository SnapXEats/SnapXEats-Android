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
public class UserCuisinePreferences {
    private String cuisine_info_id;
    private boolean is_cuisine_like;
    private boolean is_cuisine_favourite;
    private String user_id;

    @Generated(hash = 1113470737)
    public UserCuisinePreferences(String cuisine_info_id,
                                  boolean is_cuisine_like,
                                  boolean is_cuisine_favourite,
                                  String user_id) {
        this.cuisine_info_id = cuisine_info_id;
        this.is_cuisine_like = is_cuisine_like;
        this.is_cuisine_favourite = is_cuisine_favourite;
        this.user_id = user_id;
    }

    @Generated(hash = 1225303133)
    public UserCuisinePreferences() {
    }

    public String getCuisine_info_id() {
        return this.cuisine_info_id;
    }

    public void setCuisine_info_id(String cuisine_info_id) {
        this.cuisine_info_id = cuisine_info_id;
    }

    public boolean getIs_cuisine_like() {
        return this.is_cuisine_like;
    }

    public void setIs_cuisine_like(boolean is_cuisine_like) {
        this.is_cuisine_like = is_cuisine_like;
    }

    public boolean getIs_cuisine_favourite() {
        return this.is_cuisine_favourite;
    }

    public void setIs_cuisine_favourite(boolean is_cuisine_favourite) {
        this.is_cuisine_favourite = is_cuisine_favourite;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
