package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class Routes implements Parcelable {
    private String summary;

    private GoogleBounds bounds;

    private String copyrights;

    private List<String> waypoint_order;

    private List<Legs> legs;

    private List<String> warnings;

    private OverviewPolyline overview_polyline;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
        dest.writeParcelable(bounds, flags);
        dest.writeString(copyrights);
        dest.writeStringList(waypoint_order);
        dest.writeTypedList(legs);
        dest.writeStringList(warnings);
        dest.writeParcelable(overview_polyline, flags);
    }

    public Routes() {
    }

    protected Routes(Parcel in) {
        summary = in.readString();
        bounds = in.readParcelable(GoogleBounds.class.getClassLoader());
        copyrights = in.readString();
        waypoint_order = in.createStringArrayList();
        legs = in.createTypedArrayList(Legs.CREATOR);
        warnings = in.createStringArrayList();
        overview_polyline = in.readParcelable(OverviewPolyline.class.getClassLoader());
    }

    public static final Parcelable.Creator<Routes> CREATOR = new Parcelable.Creator<Routes>() {
        @Override
        public Routes createFromParcel(Parcel source) {
            return new Routes(source);
        }

        @Override
        public Routes[] newArray(int size) {
            return new Routes[size];
        }
    };
}
