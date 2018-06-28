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

    public FoodStackData(String name, String id, List<String> url, String dishId) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.dishId = dishId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.url);
        dest.writeString(this.id);
        dest.writeString(this.dishId);
    }

    protected FoodStackData(Parcel in) {
        this.name = in.readString();
        this.url = in.createStringArrayList();
        this.id = in.readString();
        this.dishId = in.readString();
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
