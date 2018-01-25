package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/1/18.
 */
@Getter
@Setter
public class RootCuisine implements Parcelable {
    private ArrayList<Cuisines> cuisineList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.cuisineList);
    }

    public RootCuisine() {
    }

    protected RootCuisine(Parcel in) {
        this.cuisineList = in.createTypedArrayList(Cuisines.CREATOR);
    }

    public static final Parcelable.Creator<RootCuisine> CREATOR = new Parcelable.Creator<RootCuisine>() {
        @Override
        public RootCuisine createFromParcel(Parcel source) {
            return new RootCuisine(source);
        }

        @Override
        public RootCuisine[] newArray(int size) {
            return new RootCuisine[size];
        }
    };
}
