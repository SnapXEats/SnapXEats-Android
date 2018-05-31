package com.snapxeats.common.model.restaurantInfo;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/2/18.
 */
@Getter
@Setter
public class RestaurantPics implements Parcelable {
    private String dish_image_url;
    private String created_date;
    private String restaurant_dish_id;
    private String audio_review_url;
    private String text_review;


    protected RestaurantPics(Parcel in) {
        dish_image_url = in.readString();
        created_date = in.readString();
        restaurant_dish_id = in.readString();
        audio_review_url = in.readString();
        text_review = in.readString();
    }

    public static final Creator<RestaurantPics> CREATOR = new Creator<RestaurantPics>() {
        @Override
        public RestaurantPics createFromParcel(Parcel in) {
            return new RestaurantPics(in);
        }

        @Override
        public RestaurantPics[] newArray(int size) {
            return new RestaurantPics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dish_image_url);
        dest.writeString(created_date);
        dest.writeString(restaurant_dish_id);
        dest.writeString(audio_review_url);
        dest.writeString(text_review);
    }
}
