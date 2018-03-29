package com.snapxeats.common.model.googleDirections;

import com.google.maps.model.Bounds;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class Routes {
    private String summary;

    private Bounds bounds;

    private String copyrights;

    private List<String> waypoint_order;

    private List<Legs> legs;

    private List<String> warnings;

    private OverviewPolyline overview_polyline;

}
