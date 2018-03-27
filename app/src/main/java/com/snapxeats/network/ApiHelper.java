package com.snapxeats.network;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.googleDirections.GeocodedWaypoints;
import com.snapxeats.common.model.location.PlaceDetail;
import com.snapxeats.common.model.location.PlacesAutoCompleteData;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
import com.snapxeats.common.model.preference.RootFoodPref;
import com.snapxeats.common.model.login.RootInstagram;
import com.snapxeats.common.model.preference.RootUserPreference;
import com.snapxeats.common.model.SnapXUserRequest;
import com.snapxeats.common.model.SnapXUserResponse;
import com.snapxeats.common.model.preference.UserPreference;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<RootCuisinePhotos> getCuisinePhotos(@Header("Authorization") String token,
                                             @Query("latitude") double latitude,
                                             @Query("longitude") double longitude,
                                             @Query("restaurant_rating") int restaurant_rating,
                                             @Query("restaurant_price") int restaurant_price,
                                             @Query("restaurant_distance") int restaurant_distance,
                                             @Query("sort_by_distance") int sort_by_distance,
                                             @Query("sort_by_rating") int sort_by_rating,
                                             @Query("cuisineArray") List<String> cuisineList,
                                             @Query("foodArray") List<String> foodArray);

    /**
     * get user info
     *
     * @param snapXUserRequest
     * @return
     */
    @POST(WebConstants.SNAPX_TOKEN)
    Call<SnapXUserResponse> getServerToken(@Body SnapXUserRequest snapXUserRequest);

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
    Call<UserPreference> setUserPreferences(@Header("Authorization") String token,
                                            @Body UserPreference object);

    /**
     * Update user preferences
     */

    @PUT(WebConstants.USER_PREFERENCES)
    Call<UserPreference> updateUserPreferences(@Header("Authorization") String token,
                                               @Body UserPreference object);

    /**
     * get restaurant details
     *
     * @param restaurantInfoId
     * @return
     */
    @GET(WebConstants.RESTAURANT_DETAILS)
    Call<RootRestaurantDetails> getRestDetails(@Path("restaurantInfoId") String restaurantInfoId);

    /**
     * get restaurant info
     *
     * @param restaurantInfoId
     * @return
     */
    @GET(WebConstants.RESTAURANT_INFO)
    Call<RootRestaurantInfo> getRestInfo(@Path("restaurantInfoId") String restaurantInfoId);

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
    Call<List<GeocodedWaypoints>> getGoogleDir(@Query("origin") String source,
                                               @Query("destination") String dest);

    /**
     * Get cuisine preferences
     *
     * @return
     */
    @GET(WebConstants.CUISINE_LIST)
    Call<RootCuisine> getCuisinePreferences();

    /**
     * foodstack gestures api
     *
     * @param token
     * @param rootFoodGestures
     * @return
     */
    @POST(WebConstants.FOODSTACK_GESTURES)
    Call<RootFoodGestures> foodstackGestures(@Header("Authorization") String token,
                                             @Body RootFoodGestures rootFoodGestures);

    /**
     * Get food preferences
     *
     * @return
     */
    @GET(WebConstants.USER_FOOD_PREF)
    Call<RootFoodPref> getFoodPreferences();

    /**
     * Get user preferences
     *
     * @param token
     * @return
     */
    @GET(WebConstants.USER_PREF)
    Call<RootUserPreference> getUserPreferences(@Header("Authorization") String token);
}
