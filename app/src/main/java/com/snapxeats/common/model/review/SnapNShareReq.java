package com.snapxeats.common.model.review;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 18/4/18.
 */

@Getter
@Setter
public class SnapNShareReq {
    private String restaurantInfoId;
    private File dishPicture;
    private File audioReview;
    private String textReview;
    private Integer rating;

    public SnapNShareReq(String restaurantInfoId, File dishPicture, File audioReview, String textReview, Integer rating) {
        this.restaurantInfoId = restaurantInfoId;
        this.dishPicture = dishPicture;
        this.audioReview = audioReview;
        this.textReview = textReview;
        this.rating = rating;
    }
}
