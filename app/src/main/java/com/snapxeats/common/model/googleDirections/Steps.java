package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Polyline;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Setter
@Getter
public class Steps implements Parcelable {
    private String html_instructions;

    private GoogleDuration duration;

    private GoogleDistance distance;

    private Polyline polyline;

    private String travel_mode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(html_instructions);
        dest.writeParcelable(duration, flags);
        dest.writeParcelable(distance, flags);
        dest.writeString(travel_mode);
    }

    public Steps() {
    }

    protected Steps(Parcel in) {
        html_instructions = in.readString();
        duration = in.readParcelable(GoogleDuration.class.getClassLoader());
        distance = in.readParcelable(GoogleDistance.class.getClassLoader());
        travel_mode = in.readString();
    }

    public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel source) {
            return new Steps(source);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
}
