package com.snapxeats.common.model;

import org.greenrobot.greendao.annotation.Entity;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Prajakta Patil on 20/2/18.
 */

@Entity
public class SnapxData {
    public String token;
    @Id
    public String userId;
    public String socialToken;
    public String socialUserId;
    public String userImage;
    public String userName;
    public String socialPlatform;
    public boolean isFirstTimeUser;

    @Generated(hash = 41198777)
    public SnapxData(String token, String userId, String socialToken,
            String socialUserId, String userImage, String userName,
            String socialPlatform, boolean isFirstTimeUser) {
        this.token = token;
        this.userId = userId;
        this.socialToken = socialToken;
        this.socialUserId = socialUserId;
        this.userImage = userImage;
        this.userName = userName;
        this.socialPlatform = socialPlatform;
        this.isFirstTimeUser = isFirstTimeUser;
    }

    @Generated(hash = 1830104362)
    public SnapxData() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSocialToken() {
        return this.socialToken;
    }

    public void setSocialToken(String socialToken) {
        this.socialToken = socialToken;
    }

    public String getSocialUserId() {
        return this.socialUserId;
    }

    public void setSocialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSocialPlatform() {
        return this.socialPlatform;
    }

    public void setSocialPlatform(String socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public boolean getIsFirstTimeUser() {
        return this.isFirstTimeUser;
    }

    public void setIsFirstTimeUser(boolean isFirstTimeUser) {
        this.isFirstTimeUser = isFirstTimeUser;
    }
}