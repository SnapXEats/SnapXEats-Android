package com.snapxeats.common.model;

import java.util.List;
import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 20/2/18.
 */
@Getter
@Setter

public class SnapxData extends RealmObject {
    public String serverToken;
    public String serverUserId;
    public String instagramToken;
    public String instagramUserId;
    public String instagramUserImage;
    public String instagramUserName;
    public String socialPlatform;
    public boolean isFirstTimeUser = false;
    public List<UserCuisinePreferences> selectedCuisinesList;
}