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
public class RootGoogleDir implements Parcelable {
    private List<GeocodedWaypoints> geocoded_waypoints;

    private String status;

    private List<Routes> routes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(geocoded_waypoints);
        dest.writeString(status);
        dest.writeList(routes);
    }

    public RootGoogleDir() {
    }

    protected RootGoogleDir(Parcel in) {
        geocoded_waypoints = new ArrayList<GeocodedWaypoints>();
        in.readList(geocoded_waypoints, GeocodedWaypoints.class.getClassLoader());
        status = in.readString();
        routes = new ArrayList<Routes>();
        in.readList(routes, Routes.class.getClassLoader());
    }

    public static final Parcelable.Creator<RootGoogleDir> CREATOR = new Parcelable.Creator<RootGoogleDir>() {
        @Override
        public RootGoogleDir createFromParcel(Parcel source) {
            return new RootGoogleDir(source);
        }

        @Override
        public RootGoogleDir[] newArray(int size) {
            return new RootGoogleDir[size];
        }
    };
}
