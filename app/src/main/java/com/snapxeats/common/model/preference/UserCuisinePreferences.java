package com.snapxeats.common.model.preference;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Snehal Tembare on 11/2/18.
 */

@Entity
public class UserCuisinePreferences {

    @Id(autoincrement = true)
    private transient Long id;
    @Expose
    private String cuisine_info_id;
    @Expose
    private boolean is_cuisine_like;
    @Expose
    private boolean is_cuisine_favourite;

    @NotNull
    private String userPreferenceId;

    @Generated(hash = 1644924845)
    public UserCuisinePreferences(String cuisine_info_id, boolean is_cuisine_like,
                                  boolean is_cuisine_favourite, @NotNull String userPreferenceId) {
        this.cuisine_info_id = cuisine_info_id;
        this.is_cuisine_like = is_cuisine_like;
        this.is_cuisine_favourite = is_cuisine_favourite;
        this.userPreferenceId = userPreferenceId;
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

    public String getUserPreferenceId() {
        return this.userPreferenceId;
    }

    public void setUserPreferenceId(String userPreferenceId) {
        this.userPreferenceId = userPreferenceId;
    }
}
