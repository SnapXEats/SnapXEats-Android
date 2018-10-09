package com.snapxeats.common.constants;


/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class WebConstants {
    public static final String BASE_URL = "https://app.snapxeats.com/";
    public static final String INSTA_CLIENT_ID = "3e7db9eb1faf436c923e4a3dfc1790fc";
    public static final String INSTA_CALLBACK_URL = "http://www.snapxeats.com";
    public static final String INSTA_AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String CUISINE_LIST = "/api/v1/cuisine/";
    public static final String CUISINE_PHOTOS = "/api/v1/Dishes";
    public static final String SNAPX_TOKEN = "/api/v1/users";
    public static final String PREDICTION_LIST =
            "/maps/api/place/autocomplete/json?types=address&components=country:us&key=AIzaSyBthIjFTzaJSyk_ySUVVJ0Jkks7AukFJZQ";
    public static final String PLACE_DETAILS =
            "/maps/api/place/details/json?key=AIzaSyBthIjFTzaJSyk_ySUVVJ0Jkks7AukFJZQ";
    public static final String GOOGLE_BASE_URL = "https://maps.googleapis.com";
    public static final String USER_PREFERENCES = "/api/v1/userPreferences";
    public static final String RESTAURANT_DETAILS = "/api/v1/restaurant/{restaurantInfoId}";
    public static final String INSTAGRAM_TOKEN = "https://api.instagram.com/v1/users/self/?";
    public static final String GOOGLE_DIR_API = "/maps/api/directions/json?mode=driving&key=AIzaSyBthIjFTzaJSyk_ySUVVJ0Jkks7AukFJZQ";
    public static final String USER_FOOD_PREF = "/api/v1/foodTypes";
    public static final String FOODSTACK_GESTURES = "/api/v1/userGesture";
    public static final String USER_PREF = "/api/v1/userPreferences";
    public static final String USER_WISHLIST = "/api/v1/userGesture/wishlist";
    public static final String USER_LOGOUT = "/api/v1/users/logout";
    public static final String CHECKIN_RESTAURANTS = "/api/v1/restaurant/checkIn/getRestaurants";
    public static final String CHECKIN = "/api/v1/restaurant/checkIn";
    public static final String SNAPSHARE = "/api/v1/snapNShare";
    public static final String FOOD_JOURNEY = "/api/v1/foodJourney";
    public static final String SMART_PHOTO = "/api/v1/dishes/{restaurant_dish_id}";
    public static final String REWARDS = "/api/v1/users/rewards";
}