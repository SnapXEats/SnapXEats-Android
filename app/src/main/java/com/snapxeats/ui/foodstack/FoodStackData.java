package com.snapxeats.ui.foodstack;

import android.os.Parcel;

import com.snapxeats.common.model.RootCuisinePhotos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
public class FoodStackData {
    public String name;
    public List<String> url;
    private String id;
    private String dishId;

    public FoodStackData(String name, String id,List<String> url,String dishId) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.dishId = dishId;
    }
}
