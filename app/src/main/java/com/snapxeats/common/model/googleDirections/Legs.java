package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class Legs implements Parcelable {

    private GoogleDuration duration;

    private GoogleDistance distance;

    private String start_address;

    private String end_address;

    private List<String> traffic_speed_entry;

    private List<String> via_waypoint;

    private List<Steps> steps;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(duration, flags);
        dest.writeParcelable(distance, flags);
        dest.writeString(start_address);
        dest.writeString(end_address);
        dest.writeStringList(traffic_speed_entry);
        dest.writeStringList(via_waypoint);
        dest.writeList(steps);
    }

    public Legs() {
    }

    protected Legs(Parcel in) {
        duration = in.readParcelable(GoogleDuration.class.getClassLoader());
        distance = in.readParcelable(GoogleDistance.class.getClassLoader());
        start_address = in.readString();
        end_address = in.readString();
        traffic_speed_entry = in.createStringArrayList();
        via_waypoint = in.createStringArrayList();
        steps = new ArrayList<Steps>();
        in.readList(steps, Steps.class.getClassLoader());
    }

    public static final Parcelable.Creator<Legs> CREATOR = new Parcelable.Creator<Legs>() {
        @Override
        public Legs createFromParcel(Parcel source) {
            return new Legs(source);
        }

        @Override
        public Legs[] newArray(int size) {
            return new Legs[size];
        }
    };
}
