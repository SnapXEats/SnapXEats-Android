package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Getter
@Setter
public class RootCuisinePhotos implements Parcelable {
    private List<DishesInfo> dishesInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.dishesInfo);
    }

    public RootCuisinePhotos() {
    }

    protected RootCuisinePhotos(Parcel in) {
        this.dishesInfo = in.createTypedArrayList(DishesInfo.CREATOR);
    }

    public static final Parcelable.Creator<RootCuisinePhotos> CREATOR = new Parcelable.Creator<RootCuisinePhotos>() {
        @Override
        public RootCuisinePhotos createFromParcel(Parcel source) {
            return new RootCuisinePhotos(source);
        }

        @Override
        public RootCuisinePhotos[] newArray(int size) {
            return new RootCuisinePhotos[size];
        }
    };
}
