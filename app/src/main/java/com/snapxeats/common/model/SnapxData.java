package com.snapxeats.common.model;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Prajakta Patil on 20/2/18.
 */

@Entity
public class SnapxData {
    public String serverToken;
    public String serverUserId;
    public String socialToken;
    public String socialUserId;
    public String userImage;
    public String userName;
    public String socialPlatform;
    public boolean isFirstTimeUser;

    //    public List<UserPreference> userPreferenceList;
//    public List<UserCuisinePreferences> selectedCuisinesList;
//    public List<UserFoodPreferences> selectedfoodList;
    @Generated(hash = 1271651620)
    public SnapxData(String serverToken, String serverUserId, String socialToken,
                     String socialUserId, String userImage, String userName,
                     String socialPlatform, boolean isFirstTimeUser) {
        this.serverToken = serverToken;
        this.serverUserId = serverUserId;
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

    public String getServerToken() {
        return this.serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getServerUserId() {
        return this.serverUserId;
    }

    public void setServerUserId(String serverUserId) {
        this.serverUserId = serverUserId;
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