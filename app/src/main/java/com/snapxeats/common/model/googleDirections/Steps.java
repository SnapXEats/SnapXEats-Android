package com.snapxeats.common.model.googleDirections;

import com.google.android.gms.maps.model.Polyline;
import com.google.maps.model.Distance;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Setter
@Getter
public class Steps {
    private String html_instructions;

    private GoogleDuration duration;

    private GoogleDistance distance;

    private End_location end_location;

    private Polyline polyline;

    private Start_location start_location;

    private String travel_mode;
}
