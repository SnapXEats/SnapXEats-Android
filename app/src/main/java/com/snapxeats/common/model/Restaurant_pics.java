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
public class Restaurant_pics implements Parcelable {
    private String dish_image_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dish_image_url);
    }

    public Restaurant_pics() {
    }

    protected Restaurant_pics(Parcel in) {
        this.dish_image_url = in.readString();
    }

    public static final Parcelable.Creator<Restaurant_pics> CREATOR = new Parcelable.Creator<Restaurant_pics>() {
        @Override
        public Restaurant_pics createFromParcel(Parcel source) {
            return new Restaurant_pics(source);
        }

        @Override
        public Restaurant_pics[] newArray(int size) {
            return new Restaurant_pics[size];
        }
    };
}
