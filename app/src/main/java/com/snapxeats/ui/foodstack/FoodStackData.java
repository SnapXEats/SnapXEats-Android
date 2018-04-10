package com.snapxeats.ui.foodstack;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
public class FoodStackData implements Parcelable {
    public String name;
    public List<String> url;
    private String id;
    private String dishId;
    private String lat;
    private String lng;

    public FoodStackData(String name, String lat, String lng, String id, List<String> url, String dishId) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.dishId = dishId;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeStringList(url);
        dest.writeString(id);
        dest.writeString(dishId);
        dest.writeString(lat);
        dest.writeString(lng);
    }

    protected FoodStackData(Parcel in) {
        name = in.readString();
        url = in.createStringArrayList();
        id = in.readString();
        dishId = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    public static final Parcelable.Creator<FoodStackData> CREATOR = new Parcelable.Creator<FoodStackData>() {
        @Override
        public FoodStackData createFromParcel(Parcel source) {
            return new FoodStackData(source);
        }

        @Override
        public FoodStackData[] newArray(int size) {
            return new FoodStackData[size];
        }
    };
}
