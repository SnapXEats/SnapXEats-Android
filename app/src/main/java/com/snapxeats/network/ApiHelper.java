package com.snapxeats.network;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.PlaceDetail;
import com.snapxeats.common.model.PlacesAutoCompleteData;
import com.snapxeats.common.model.RootCuisine;

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
    Call<RootCuisine> getCuisineList();

    /**
     * Get predection list
     *
     * @param input-to search place according to input
     * @return
     */
    @GET(WebConstants.PREDICTION_LIST)
    Call<PlacesAutoCompleteData> getPredictionList(@Query("input") String input);

    /**
     * Get place details
     *
     * @param placeId- to search place details
     * @return
     */
    @GET(WebConstants.PLACE_DETAILS)
    Call<PlaceDetail> getPlaceDetails(@Query("placeid") String placeId);
}
