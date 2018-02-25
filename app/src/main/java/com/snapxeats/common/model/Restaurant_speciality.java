package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class Restaurant_speciality implements Parcelable {
    private String dish_image_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dish_image_url);
    }

    public Restaurant_speciality() {
    }

    protected Restaurant_speciality(Parcel in) {
        this.dish_image_url = in.readString();
    }

    public static final Parcelable.Creator<Restaurant_speciality> CREATOR = new Parcelable.Creator<Restaurant_speciality>() {
        @Override
        public Restaurant_speciality createFromParcel(Parcel source) {
            return new Restaurant_speciality(source);
        }

        @Override
        public Restaurant_speciality[] newArray(int size) {
            return new Restaurant_speciality[size];
        }
    };
}
