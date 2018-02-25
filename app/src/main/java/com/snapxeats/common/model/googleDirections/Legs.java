package com.snapxeats.common.model.googleDirections;

import com.google.maps.model.Distance;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class Legs {

    private GoogleDuration duration;

    private GoogleDistance distance;

    private End_location end_location;

    private String start_address;

    private String end_address;

    private Start_location start_location;

    private List<String> traffic_speed_entry;

    private List<String> via_waypoint;

    private List<Steps> steps;

}
