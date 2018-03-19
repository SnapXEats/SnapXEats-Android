package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.snapxeats.common.model.restaurantDetails.RestaurantPics;
import com.snapxeats.common.model.restaurantDetails.RestaurantTimings;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 26/2/18.
 */
@Getter
@Setter
public class RestaurantInfo{
    private List<String> restaurant_aminities;

    private List<RestaurantTimings> restaurant_timings;

    private String isOpenNow;

    private String restaurant_info_id;

    private List<RestaurantPics> restaurant_pics;

    private String restaurant_address;

    private String restaurant_name;
}
