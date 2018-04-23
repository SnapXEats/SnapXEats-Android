package com.snapxeats.common.constants;

/**
 * Created by Prajakta Patil on 11/4/18.
 */
public class UIConstants {

    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static final int PROFILE_WIDTH_HEIGHT = 50;
    public static final int PROFILE_HEIGHT = 50;

    /*Maps Screen*/
    public static final String LATITUDE = "40.4862157";
    public static final String LONGITUDE = "-74.4518188";

    public static final double LAT =  40.7014;
    public static final double LNG = -74.0151;

    public static final float DEFAULT = 0;
    public static final float MAP_SCOLL_BY = 370;
    public static final float MAP_ZOOM = 12.3f;
    public static final int MAP_FILL_COLOR = 0x55AAAAAA;
    public static final float MAP_STROKE = 5;
    public static final float MAP_MARKER_ZOOM = 13;
    public static final String DIST_FORMAT ="##.###";

    /*Directions Screen*/
    public static final float SCROLL_MIN_SCALE = 0.8f;
    public static final float DIST_IN_MILES = (float) 1609.34;
    public static final float ROUTE_WIDTH = 10;
    public static final int ROUTE_COLOR = 93;
    public static final long DURATION_MARKER = 30000;

    /*Foodstack screen*/
    public static final long SET_START_DELAY = 100;
    public static final long SET_DURATION = 500;
    public static final long SET_ROTATION_DURATION = 200;
    public static final float SET_ALPHA = (float) 1.0;
    public static final float LEFT_ROTATION = -10f;
    public static final float LEFT_X_TRANSLATION = -2000f;
    public static final float LEFT_Y_TRANSLATION = 500f;
    public static final float DEFAULT_TRANSLATION = 0f;
    public static final float TOP_Y_TRANSLATION = -500f;
    public static final float SET_ALPHA_DISABLE = (float) 0.5;
    public static final float RIGHT_ROTATION = 10f;
    public static final float RIGHT_X_TRANSLATION = 2000f;
    public static final float RIGHT_Y_TRANSLATION = 500f;
    public static final int CARD_COUNT = 5;

    /*Wishlist screen*/
    public static final String DELETE = "Delete";
    public static final String EDIT = "Edit";

    /*GetPredicationTask class*/
    public static final String TAG_PREDICTION = "GetPredictionTask";
    public static final String PLACES_API_BASE = WebConstants.GOOGLE_BASE_URL + WebConstants.PREDICTION_LIST;

    /*FoodPrefAdapter*/
    public static final int SINGLE_TAP = 1;
    public static final int DOUBLE_TAP = 2;

    /*HomeInteractor*/
    public static final int ACCESS_FINE_LOCATION = 1;
    public static final int DEVICE_LOCATION = 2;
    public static final int ROUTE_PADDING = (int) 0.80;
    public static final int CHECKIN_DIALOG_WIDTH = 950;
    public static final int CHECKIN_DIALOG_HEIGHT = 1250;
    public static final int REWARD_DIALOG_WIDTH = 800;
    public static final int REWARD_DIALOG_HEIGHT = 1100;

    /*Snap N Share*/
    public static final int MARGIN = 8;

    public static final  int CAMERA_REQUEST_PERMISSION = 3;
    public static final  int EXTERNAL_STORAGE_PERMISSION = 5;
    public static final  int CAMERA_REQUEST = 4;
}
