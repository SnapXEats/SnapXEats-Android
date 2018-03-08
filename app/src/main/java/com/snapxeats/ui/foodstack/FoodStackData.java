package com.snapxeats.ui.foodstack;

import com.snapxeats.common.model.RootCuisinePhotos;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
public class FoodStackData extends RootCuisinePhotos {
    public String name;
    String url;
    private String id;

    FoodStackData(String name, String url, String id) {
        this.name = name;
        this.url = url;
        this.id = id;
    }
}
