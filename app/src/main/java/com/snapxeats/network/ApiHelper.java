package com.snapxeats.network;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.PlaceDetail;
import com.snapxeats.common.model.PlacesAutoCompleteData;
import com.snapxeats.common.model.RootCuisine;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.RootFoodPref;
import com.snapxeats.common.model.RootInstagram;
import com.snapxeats.common.model.RootRestaurantDetails;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.UserPreference;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    /**
     * get user info
     *
     * @param snapXUserRequest
     * @return
     */
    @POST(WebConstants.SNAPX_TOKEN)
    Call<SnapXUserResponse> getUserToken(@Body SnapXUserRequest snapXUserRequest);

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


    /**
     * Set user preferences
     *
     * @param object- Set user preferences
     * @return
     */
    @POST(WebConstants.USER_PREFERENCES)
    Call<UserPreference> setUserPreferences(@Body UserPreference object);

    /**
     * get restaurant details
     *
     * @param restaurantInfoId
     * @return
     */
    @GET(WebConstants.RESTAURANT_DETAILS)
    Call<RootRestaurantDetails> getRestDetails(@Path("restaurantInfoId") String restaurantInfoId);

    /**
     * get instagram info
     *
     * @param accessToken
     * @return
     */
    @GET(WebConstants.INSTAGRAM_TOKEN)
    Call<RootInstagram> getInstagramInfo(@Query("access_token") String accessToken);

    /**
     * get google directions
     *
     * @param source
     * @param dest
     * @return
     */
    @GET(WebConstants.GOOGLE_DIR_API)
    Call<RootGoogleDir> getGoogleDir(@Query("origin") String source,
                                     @Query("destination") String dest);

    /**
     * Get cuisine preferences
     *
     * @return
     */
    @GET(WebConstants.CUISINE_LIST)
    Call<RootCuisine> getCuisinePreferences();

    /**
     * Get food preferences
     *
     * @return
     */
    @GET(WebConstants.USER_FOOD_PREF)
    Call<RootFoodPref> getFoodPreferences();
}
