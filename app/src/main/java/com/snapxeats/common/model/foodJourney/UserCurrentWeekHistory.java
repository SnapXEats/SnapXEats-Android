package com.snapxeats.common.model.foodJourney;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/5/18.
 */
@Getter
@Setter
public class UserCurrentWeekHistory {
    private String reward_point;

    private String restaurant_info_id;

    private String formattedDate;

    private String[] reward_dishes;

    private String restaurant_image_url;

    private String restaurant_address;

    private String restaurant_name;

}
