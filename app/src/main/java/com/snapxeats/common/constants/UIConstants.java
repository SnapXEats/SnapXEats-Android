package com.snapxeats.common.constants;

/**
 * Created by Prajakta Patil on 11/4/18.
 */
public class UIConstants {

    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static final int PROFILE_WIDTH_HEIGHT = 50;
    public static final int PADDING = 20;
    public static final int WEBVIEW_SCALE = 210;
    public static final String SX_VERSION = "V ";
    public static final String SNAPX_DB_NAME = "SnapXDb";
    public static final String PACKAGE = "package";

    /*NavPrefFragment*/
    public static final int PRICE_AUTO = 0;
    public static final int PRICE_ONE = 1;
    public static final int PRICE_TWO = 2;
    public static final int PRICE_THREE = 3;
    public static final int PRICE_FOUR = 4;

    public static final int THREE_STAR = 3;
    public static final int FOUR_STAR = 4;
    public static final int FIVE_STAR = 5;

    public static final int DISTANCE_ONE = 1;
    public static final int DISTANCE_TWO = 2;
    public static final int DISTANCE_THREE = 3;
    public static final int DISTANCE_FOUR = 4;
    public static final int DISTANCE_FIVE = 5;

    /*Maps Screen*/
    public static final String LATITUDE = "40.4862157";
    public static final String LONGITUDE = "-74.4518188";

    public static final double LAT = 40.7014;
    public static final double LNG = -74.0151;

    public static final float DEFAULT = 0;
    public static final float MAP_SCOLL_BY = 320;
    public static final float MAP_ZOOM = 13.2f;
    public static final int MAP_FILL_COLOR = 0x55AAAAAA;
    public static final float MAP_STROKE = 5;
    public static final float MAP_MARKER_ZOOM = 13;
    public static final String DIST_FORMAT = "##.###";

    /*Directions Screen*/
    public static final float SCROLL_MIN_SCALE = 0.8f;
    public static final float DIST_IN_MILES = (float) 1609.34;
    public static final float ROUTE_WIDTH = 10;
    public static final int ROUTE_COLOR = 93;
    public static final long DURATION_MARKER = 30000;
    public static final String DIR_PRICE_ONE = "1";
    public static final String DIR_PRICE_TWO = "2";
    public static final String DIR_PRICE_THREE = "3";
    public static final String DIR_PRICE_FOUR = "4";

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
    public static final int CHECKIN_DIALOG_WIDTH = 950;
    public static final int CHECKIN_DIALOG_HEIGHT = 1250;
    public static final int CHECKIN_SINGLE_ITEM_DIALOG_HEIGHT = 1000;
    public static final int CHECKIN_SINGLE_ITEM_DIALOG_WIDTH = 850;
    public static final int NO_DATA_DIALOG_HEIGHT = 1250;
    public static final int NO_DATA_DIALOG_WIDTH = 900;
    public static final int REWARD_DIALOG_WIDTH = 800;
    public static final int REWARD_DIALOG_HEIGHT = 1100;
    public static final int LOGOUT_DIALOG_HEIGHT = 500;

    public static final String ACTION_LOCATION_GET = "snapxeats.GET_LOCATION";
    public static final int REQUEST_PERMISSION_CODE = 1;

    /*Snap N Share*/
    public static final int MARGIN = 8;

    /*AddReview screen*/
    public static final String INSTA_PACKAGE_NAME = "com.instagram.android";
    public static final String IMAGE_TYPE = "image/*";
    public static final String IMAGE = "image/";
    public static final String FILE_MEDIATYPE = "*/*";
    public static final String TEXT_TYPE = "text/plain";
    public static final String FB_SHA = "SHA";
    public static final String SX_BEARER = "Bearer ";

    public static final int CAMERA_REQUEST_PERMISSION = 3;
    public static final int CAMERA_REQUEST = 4;
    public static final String NOTIFICATION_MESSAGE = "Its Time to take some food snap " +
            "\nBy sharing photos you can earn rewards";
    public static final String CHECKIN_NOTIFICATION_MESSAGE = "CheckIn and earn reward points";
    public static final int NOTIFICATION_ID = 101;

    public static final int REVIEW_LENGTH_LIMIT = 140;
    public static final int CHECKOUT_DURATION = 2;
    public static final int TIME_HOUR = 3600000;
    public static final int TIME_MINUTE = 60000;
    public static final int TIME_SECONDS = 1000;
    public static final int INT_TEN = 10;
    public static final long PHOTO_NOTIFICATION_TIME = 10;
    public static final int PHOTO_NOTIFICATION_REQUEST_CODE = 0;
    public static final int REQUEST_CODE_TAKE_PHOTO_ACTION = 2;
    public static final int REQUEST_CODE_REMIND_ACTION = 4;
    public static final int REMIND_LATER_REQUEST_ACTION = 6;
    public static final long MILLI_TO_SEC = 1000;
    public static final long MILLI_TO_SEC_CONVERSION = 1000;
    public static final long MILLIS = 1000 * 60 * 60;
    public static final long MILLIES_TWO = 1000 * 60;
    public static final long TEN = 10;
    public static final float THUMBNAIL = 0.5f;
    public static final double UNSELECT_OPACITY = 1.0;
    public static final double SELECT_OPACITY = 0.4;
    public static final int PERCENTAGE = 100;
    public static final int STORAGE_REQUEST_PERMISSION = 8;
    public static final int CHANGE_LOCATION_PERMISSIONS = 9;
    public static final int BUFFER_SIZE = 8192;
    public static final int BYTES = 1024;
    public static final long TIME_DELAY = 250;
    public static final int DIALOG_Y_POSITION = 120;
    public static final int CHECKIN_NOTIFICATION_REQUEST_CODE = 1;
    public static final int CHECKIN_NOTIFICATION_TIME = 2;
    public static final int REQUEST_CODE_CHECKIN_ACTION = 5;
    public static final int CHECKIN_NOTIFICATION_ID = 201;
}
