package com.snapxeats.common.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prajakta Patil on 7/2/18.
 */

@Entity
public class SnapXUser {
    private String token;
    private String user_id;
    private String social_platform;
    private boolean isFirstTimeUser;

    @Generated(hash = 1772475278)
    public SnapXUser(String token, String user_id, String social_platform,
            boolean isFirstTimeUser) {
        this.token = token;
        this.user_id = user_id;
        this.social_platform = social_platform;
        this.isFirstTimeUser = isFirstTimeUser;
    }

    @Generated(hash = 972905057)
    public SnapXUser() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSocial_platform() {
        return this.social_platform;
    }

    public void setSocial_platform(String social_platform) {
        this.social_platform = social_platform;
    }

    public boolean getIsFirstTimeUser() {
        return this.isFirstTimeUser;
    }

    public void setIsFirstTimeUser(boolean isFirstTimeUser) {
        this.isFirstTimeUser = isFirstTimeUser;
    }
}
