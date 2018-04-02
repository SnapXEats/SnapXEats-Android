package com.snapxeats.common.model.foodGestures;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 24/3/18.
 */

@Getter
@Setter

public class Wishlist {
    private String user_gesture_id;
    private String restaurant_dish_id;
    private String restaurant_info_id;
    private String restaurant_name;
    private String restaurant_address;
    private String dish_image_url;
    private String created_at;
    private boolean isDeleted;
}
