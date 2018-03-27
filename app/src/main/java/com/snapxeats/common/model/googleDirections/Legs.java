package com.snapxeats.common.model.googleDirections;

import java.util.ArrayList;
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

    private EndLocation end_location;

    private String start_address;

    private String end_address;

    private StartLocation start_location;

//    private String[] traffic_speed_entry;
    private List<String> traffic_speed_entry;

    private List<String> via_waypoint;
//    private String[] via_waypoint;

//    private Steps[] steps;
    private List<Steps> steps;

}
