package com.snapxeats.common.model.restaurantDetails;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class RestaurantDetails {
    private String location_lat;

    private List<RestaurantTimings> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<RestaurantSpeciality> restaurant_speciality;

    private List<RestaurantPics> restaurant_pics;

    private String restaurant_address;

    private String restaurant_name;

    private String restaurant_contact_no;

    private String location_long;
}
