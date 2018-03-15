package com.snapxeats.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/2/18.
 */
@Getter
@Setter
public class SelectedCuisineList implements Parcelable {
    LocationCuisine location;
    int restaurant_distance;
    int restaurant_price;
    int restaurant_rating;
    int sort_by_rating;
    int sort_by_distance;
    List<String> selectedCuisineList;
    List<String> selectedFoodList;

    public SelectedCuisineList() {
    }

    public SelectedCuisineList(LocationCuisine location,
                               int restaurant_distance,
                               int restaurant_price,
                               int restaurant_rating,
                               int sort_by_rating,
                               int sort_by_distance,
                               List<String> selectedCuisineList,
                               List<String> selectedFoodList) {
        this.location = location;
        this.restaurant_distance = restaurant_distance;
        this.restaurant_price = restaurant_price;
        this.restaurant_rating = restaurant_rating;
        this.sort_by_rating = sort_by_rating;
        this.sort_by_distance = sort_by_distance;
        this.selectedCuisineList = selectedCuisineList;
        this.selectedFoodList = selectedFoodList;
    }

    public SelectedCuisineList(Parcel in) {
        location = in.readParcelable(LocationCuisine.class.getClassLoader());
        restaurant_distance = in.readInt();
        restaurant_price = in.readInt();
        restaurant_rating = in.readInt();
        sort_by_rating = in.readInt();
        sort_by_distance = in.readInt();
        selectedCuisineList = in.createStringArrayList();
        selectedFoodList = in.createStringArrayList();
    }

    public static final Creator<SelectedCuisineList> CREATOR = new Creator<SelectedCuisineList>() {
        @Override
        public SelectedCuisineList createFromParcel(Parcel in) {
            return new SelectedCuisineList(in);
        }

        @Override
        public SelectedCuisineList[] newArray(int size) {
            return new SelectedCuisineList[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
        dest.writeInt(restaurant_distance);
        dest.writeInt(restaurant_price);
        dest.writeInt(restaurant_rating);
        dest.writeInt(sort_by_rating);
        dest.writeInt(sort_by_distance);
        dest.writeStringList(selectedCuisineList);
        dest.writeStringList(selectedFoodList);
    }
}
