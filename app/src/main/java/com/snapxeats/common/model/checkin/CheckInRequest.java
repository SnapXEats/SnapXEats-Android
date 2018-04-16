package com.snapxeats.common.model.checkin;

import com.snapxeats.common.model.restaurantDetails.RestaurantDishes;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 10/4/18.
 */

@Getter
@Setter

public class CheckInRequest {
     private  String restaurant_info_id;
     private  String reward_type;
     private List<RestaurantDishes> restaurantDishes;
}
