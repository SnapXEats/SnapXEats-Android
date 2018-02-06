package com.snapxeats.network;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.RootCuisinePhotos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public interface ApiHelper {

    /**
     * get cuisine list
     *
     * @return
     */
    @GET(WebConstants.CUISINE_LIST)
    Call<RootCuisine> getCuisineList(@Query("latitude") double latitude,
                                     @Query("longitude") double longitude);

    /**
     * get cuisine photos
     *
     * @return
     */
    @GET(WebConstants.CUISINE_PHOTOS)
    Call<RootCuisinePhotos> getCuisinePhotos(@Query("latitude") double latitude,
                                             @Query("longitude") double longitude,
                                             @Query("cuisineArray") List<String> cuisineList);
}
