package com.snapxeats.common.model.googleDirections;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class Geocoded_waypoints {
    private String place_id;

    private String geocoder_status;

    private List<String> types;
}
