package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 20/2/18.
 */
@Getter
@Setter
public class RootGoogleDir {
    private List<GeocodedWaypoints> geocoded_waypoints;

    private String status;

    private List<Routes> routes;
}
