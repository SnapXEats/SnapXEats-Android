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
public class GeocodedWaypoints implements Parcelable {
    private String place_id;

    private String geocoder_status;

    private List<String> types;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place_id);
        dest.writeString(geocoder_status);
        dest.writeStringList(types);
    }

    public GeocodedWaypoints() {
    }

    protected GeocodedWaypoints(Parcel in) {
        place_id = in.readString();
        geocoder_status = in.readString();
        types = in.createStringArrayList();
    }

    public static final Parcelable.Creator<GeocodedWaypoints> CREATOR = new Parcelable.Creator<GeocodedWaypoints>() {
        @Override
        public GeocodedWaypoints createFromParcel(Parcel source) {
            return new GeocodedWaypoints(source);
        }

        @Override
        public GeocodedWaypoints[] newArray(int size) {
            return new GeocodedWaypoints[size];
        }
    };
}
