package com.snapxeats.common.model;


/**
 * Created by Prajakta Patil on 7/2/18.
 */

public class SnapXUserRequest {
    private String access_token;
    private String social_platform;
    private String social_id;

    /**
     * constructor for SnapXUser
     * @param access_token
     * @param social_platform
     * @param social_id
     */
    public SnapXUserRequest(String access_token, String social_platform, String social_id) {
        this.access_token = access_token;
        this.social_platform = social_platform;
        this.social_id = social_id;
    }
}
