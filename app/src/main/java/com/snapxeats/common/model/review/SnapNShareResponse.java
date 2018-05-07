package com.snapxeats.common.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 18/4/18.
 */
@Getter
@Setter

public class SnapNShareResponse implements Parcelable {
    private String restaurant_name;
    private String restaurant_dish_id;
    private String dish_image_url;
    private String message;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurant_name);
        dest.writeString(restaurant_dish_id);
        dest.writeString(dish_image_url);
        dest.writeString(message);
    }

    private SnapNShareResponse(Parcel in) {
        restaurant_name = in.readString();
        restaurant_dish_id = in.readString();
        dish_image_url = in.readString();
        message = in.readString();
    }

    public static final Parcelable.Creator<SnapNShareResponse> CREATOR = new Parcelable.Creator<SnapNShareResponse>() {
        @Override
        public SnapNShareResponse createFromParcel(Parcel source) {
            return new SnapNShareResponse(source);
        }

        @Override
        public SnapNShareResponse[] newArray(int size) {
            return new SnapNShareResponse[size];
        }
    };
}
