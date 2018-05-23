package com.snapxeats.common.model.smartphotos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 23/5/18.
 */

@Getter
@Setter
public class SmartPhotoResponse {
    private String restaurant_dish_id;
    private String restaurant_name;
    private String restaurant_address;
    private String dish_image_url;
    private String pic_taken_date;
    private String audio_review_url;
    private String text_review;
    private List<String> restaurant_aminities;
}
