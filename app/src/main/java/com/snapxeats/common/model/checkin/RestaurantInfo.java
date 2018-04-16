package com.snapxeats.common.model.checkin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 9/4/18.
 */

@Getter
@Setter

public class RestaurantInfo {
    private String restaurant_info_id;
    private String restaurant_name;
    private String restaurant_type;
    private String restaurant_logo;
    private boolean isSelected;
}
