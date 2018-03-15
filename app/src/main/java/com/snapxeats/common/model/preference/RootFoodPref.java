package com.snapxeats.common.model.preference;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 21/2/18.
 */

@Getter
@Setter

public class RootFoodPref implements Parcelable{
    private List<FoodPref> foodTypeList;

    protected RootFoodPref(Parcel in) {
        foodTypeList = in.createTypedArrayList(FoodPref.CREATOR);
    }

    public static final Creator<RootFoodPref> CREATOR = new Creator<RootFoodPref>() {
        @Override
        public RootFoodPref createFromParcel(Parcel in) {
            return new RootFoodPref(in);
        }

        @Override
        public RootFoodPref[] newArray(int size) {
            return new RootFoodPref[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(foodTypeList);
    }
}
