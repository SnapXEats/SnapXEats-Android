package com.snapxeats.common.model.googleDirections;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class OverviewPolyline implements Parcelable {
    private String points;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(points);
    }

    public OverviewPolyline() {
    }

    protected OverviewPolyline(Parcel in) {
        points = in.readString();
    }

    public static final Parcelable.Creator<OverviewPolyline> CREATOR = new Parcelable.Creator<OverviewPolyline>() {
        @Override
        public OverviewPolyline createFromParcel(Parcel source) {
            return new OverviewPolyline(source);
        }

        @Override
        public OverviewPolyline[] newArray(int size) {
            return new OverviewPolyline[size];
        }
    };
}
