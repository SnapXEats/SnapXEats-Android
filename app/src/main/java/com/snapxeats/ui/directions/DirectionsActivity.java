package com.snapxeats.ui.directions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pkmmte.view.CircularImageView;
import com.snapxeats.BaseActivity;
import com.snapxeats.R;
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.checkin.CheckInRequest;
import com.snapxeats.common.model.checkin.CheckInResponse;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantInfo.RootRestaurantInfo;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.ui.home.fragment.checkin.CheckInDbHelper;
import com.snapxeats.ui.home.fragment.snapnshare.SnapNotificationReceiver;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snapxeats.common.constants.UIConstants.CHECKIN_NOTIFICATION_REQUEST_CODE;
import static com.snapxeats.common.constants.UIConstants.CHECKIN_NOTIFICATION_TIME;
import static com.snapxeats.common.constants.UIConstants.DIR_PRICE_FOUR;
import static com.snapxeats.common.constants.UIConstants.DIR_PRICE_ONE;
import static com.snapxeats.common.constants.UIConstants.DIR_PRICE_THREE;
import static com.snapxeats.common.constants.UIConstants.DIR_PRICE_TWO;
import static com.snapxeats.common.constants.UIConstants.ONE;
import static com.snapxeats.common.constants.UIConstants.STRING_SPACE;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Prajakta Patil on 22/3/18.
 */
public class DirectionsActivity extends BaseActivity
        implements DirectionsContract.DirectionsView, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    @Inject
    SnapXDialog snapXDialog;

    @Inject
    AppUtility utility;

    @Inject
    DirectionsContract.DirectionsPresenter mPresenter;

    @BindView(R.id.txtDirRestName)
    protected TextView mTxtRestName;

    @BindView(R.id.txtDirRestAddr)
    protected TextView mTxtRestAddr;

    @BindView(R.id.txtDirRating)
    protected TextView mTxtRating;

    @BindView(R.id.txt_price_maps)
    protected TextView mTxtPrice;

    @BindView(R.id.txt_distance_maps)
    protected TextView mTxtRestDist;

    private RootRestaurantInfo mDetails;

    private GoogleMap mMap;

    private List<LatLng> list;

    @BindView(R.id.spinner_directions)
    protected Spinner mDirSpinner;

    @BindView(R.id.txt_dir_rest_open)
    protected TextView mTxtDirRestOpen;

    @BindView(R.id.txt_dir_rest_close)
    protected TextView mTxtDirRestClose;

    @BindView(R.id.toolbar_directions)
    protected Toolbar mToolbar;

    private Location mCurrentLocation;

    private Dialog dialog;

    @Inject
    CheckInDbHelper checkInDbHelper;

    private String restId;

    private String userId, currentTime;

    private boolean isCheckedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * Set up map view
     **/
    private void setMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_directions);
        mapFragment.getMapAsync(DirectionsActivity.this);
    }

    /**
     * Initialize components
     **/
    @Override
    public void initView() {
        mPresenter.addView(this);
        snapXDialog.setContext(this);
        utility.setContext(this);
        checkInDbHelper.setContext(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.directions));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utility.enableReceiver(new ComponentName(this, SnapNotificationReceiver.class));
        setMapView();
        setUpViews();
        mCurrentLocation = utility.getLocation();

        //shows check in dialog, on checkin notification action
        boolean isCheckInNotification = getIntent().getBooleanExtra(getString(R.string.intent_dir_check_in), false);
        if (isCheckInNotification) {
            dialogCheckIn();
        }
    }

    private void dialogCheckIn() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_auto_checkin, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();

        Button mBtnCheckIn = alertLayout.findViewById(R.id.btn_check_in);
        TextView mTxtCancel = alertLayout.findViewById(R.id.txt_cancel);
        TextView mTxtRestName = alertLayout.findViewById(R.id.txt_rest_name);
        CircularImageView mImgRest = alertLayout.findViewById(R.id.img_restaurant);

        mTxtRestName.setText(mDetails.restaurantDetails.getRestaurant_name());
        Glide.with(this)
                .load(mDetails.getRestaurantDetails().getRestaurant_pics().get(ZERO).getDish_image_url())
                .placeholder(R.drawable.ic_restaurant_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(THUMBNAIL)
                .into(mImgRest);

        mBtnCheckIn.setOnClickListener(v -> {
            showProgressDialog();
            CheckInRequest checkInRequest = new CheckInRequest();
            checkInRequest.setRestaurant_info_id(mDetails.getRestaurantDetails().getRestaurant_info_id());
            checkInRequest.setReward_type(getString(R.string.reward_type_check_in));
            mPresenter.checkIn(checkInRequest);
        });

        mTxtCancel.setOnClickListener(v -> dialog.dismiss());

    }

    //tracks if current location and restaurant location are equal or not
    @Override
    public void onMyLocationChange(Location location) {
        Location target = new Location("target");
        if (null != mCurrentLocation && null != mDetails.getRestaurantDetails()) {
            LatLng currentLoc = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            LatLng destLoc = new LatLng(Double.valueOf(mDetails.getRestaurantDetails().getLocation_lat()),
                    Double.valueOf(mDetails.getRestaurantDetails().getLocation_long()));
            for (LatLng point : list) {
                target.setLatitude(point.latitude);
                target.setLongitude(point.longitude);
                if (currentLoc == destLoc) {
                    dialogCheckIn();
                }
            }
        }
    }

    /**
     * Set values for components
     **/
    private void setUpViews() {
        mDetails = getIntent().getExtras().getParcelable(getString(R.string.intent_rest_details));
        if (null != mDetails && null != mDetails.getRestaurantDetails().getRestaurant_info_id()) {
            restId = mDetails.getRestaurantDetails().getRestaurant_info_id();
        }
        if (null != mDetails && null != mDetails.getRestaurantDetails()) {
            mTxtRestName.setText(String.valueOf(mDetails.getRestaurantDetails().getRestaurant_name()));
            mTxtRestAddr.setText(mDetails.getRestaurantDetails().getRestaurant_address());
            mTxtRating.setText(mDetails.getRestaurantDetails().getRestaurant_rating());
            distInMiles(Double.valueOf(UIConstants.LATITUDE),
                    Double.valueOf(UIConstants.LONGITUDE),
                    Double.valueOf(mDetails.getRestaurantDetails().getLocation_lat()),
                    Double.valueOf(mDetails.getRestaurantDetails().getLocation_long()));
            setRestTimings();
            setRestPrice();
        }
    }

    private void setRestPrice() {
        String price = mDetails.getRestaurantDetails().getRestaurant_price();
        switch (price) {
            case DIR_PRICE_ONE:
                mTxtPrice.setText(getString(R.string.price_one));
                break;
            case DIR_PRICE_TWO:
                mTxtPrice.setText(getString(R.string.price_two));
                break;
            case DIR_PRICE_THREE:
                mTxtPrice.setText(getString(R.string.price_three));
                break;
            case DIR_PRICE_FOUR:
                mTxtPrice.setText(getString(R.string.price_four));
                break;
            default:
                mTxtPrice.setText(getString(R.string.price_one));
        }
    }

    private void setRestTimings() {
        List<String> listTimings = new ArrayList<>();
        String isOpenNow = mDetails.getRestaurantDetails().getIsOpenNow();
        if (null != mDetails && ZERO != mDetails.getRestaurantDetails().getRestaurant_timings().size()) {
            for (int row = ZERO; row < mDetails.getRestaurantDetails().getRestaurant_timings().size(); row++) {
                listTimings.add(mDetails.getRestaurantDetails().getRestaurant_timings().get(row).getDay_of_week() +
                        "         " +
                        mDetails.getRestaurantDetails().getRestaurant_timings().get(row).getRestaurant_open_close_time());
            }
            Comparator<String> dateComparator = (s1, s2) -> {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format_month));
                    Date d1 = format.parse(s1);
                    Date d2 = format.parse(s2);
                    if (d1.equals(d2)) {
                        return s1.substring(s1.indexOf(STRING_SPACE) + ONE).compareTo(s2.substring(s2.indexOf(STRING_SPACE) + ONE));
                    } else {
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(d1);
                        cal2.setTime(d2);
                        return cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
                    }
                } catch (ParseException pe) {
                    throw new RuntimeException(pe);
                }
            };
            Collections.sort(listTimings, dateComparator);
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listTimings);
            mDirSpinner.setAdapter(adapter);
        } else if (isOpenNow.equalsIgnoreCase("true")) {
            mDirSpinner.setVisibility(View.GONE);
            mTxtDirRestOpen.setVisibility(View.VISIBLE);
        } else {
            mDirSpinner.setVisibility(View.GONE);
            mTxtDirRestClose.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set route on google map
     **/
    public void setGoogleRoute() {
        RootGoogleDir mGoogleDir = getIntent().getExtras().getParcelable(getString(R.string.intent_google_dir));
        //TODO latlng are hardcoded
        LatLng src = new LatLng(Double.parseDouble(UIConstants.LATITUDE), Double.parseDouble(UIConstants.LONGITUDE));

        mMap.addMarker(new MarkerOptions().position(src)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_pin)));
        LatLng destination = new LatLng(Double.parseDouble(mDetails.getRestaurantDetails().getLocation_lat()),
                Double.parseDouble(mDetails.getRestaurantDetails().getLocation_long()));
        mMap.addMarker(new MarkerOptions().position(destination)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_dest)));
        if (null != mGoogleDir) {
            String encodedString = mGoogleDir.getRoutes().get(ZERO).getOverview_polyline().getPoints();
            list = decodePoly(encodedString);

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(DirectionsActivity.this, R.color.colorBlack));
            polyOptions.addAll(list);

            mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(UIConstants.ROUTE_WIDTH)
                    .color(Color.rgb(UIConstants.ROUTE_COLOR, UIConstants.ROUTE_COLOR, UIConstants.ROUTE_COLOR))
                    .geodesic(true));
            zoomRoute(mMap, list);
        }
    }

    /* Fetch latlng list of points for setting route */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = ZERO, len = encoded.length();
        int lat = ZERO, lng = ZERO;

        while (index < len) {
            int b, shift = ZERO, result = ZERO;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & ONE) != 0 ? ~(result >> ONE) : (result >> ONE));
            lat += dlat;

            shift = ZERO;
            result = ZERO;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & ONE) != 0 ? ~(result >> ONE) : (result >> ONE));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    /* Set animation for google moving marker */
    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final BitmapDescriptor bitmap) {
        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(bitmap)
                .position(directionPoint.get(ZERO))
                .flat(true));
        animateMarker(myMap, marker, directionPoint, false);
    }

    /* Set up google moving marker */
    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int point = ZERO;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / UIConstants.DURATION_MARKER);
                if (point < directionPoint.size())
                    marker.setPosition(directionPoint.get(point));
                point++;
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setGoogleRoute();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
    }

    /* Zoom over google direction route to fit screen */
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
        if (null != googleMap || null != lstLatLngRoute || !lstLatLngRoute.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng latLngPoint : lstLatLngRoute)
                boundsBuilder.include(latLngPoint);
            int routePadding = 100;
            LatLngBounds latLngBounds = boundsBuilder.build();
            mMap.setOnMapLoadedCallback(() -> googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding)));
        }
    }

    //calculate distance in miles
    private void distInMiles(double srcLlat, double srcLng, double destLat, double destLng) {
        double theta = srcLng - destLng;
        double dist = Math.sin(deg2rad(srcLlat))
                * Math.sin(deg2rad(destLat))
                + Math.cos(deg2rad(srcLlat))
                * Math.cos(deg2rad(destLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        NumberFormat distance = new DecimalFormat(UIConstants.DIST_FORMAT);
        mTxtRestDist.setText(distance.format(dist) + STRING_SPACE + getString(R.string.mi));
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void success(Object value) {
        isCheckedIn = true;
        dismissProgressDialog();
        dialog.dismiss();
        getCheckedInData();
        if (utility.isLoggedIn()) {
            checkInDbHelper.saveCheckInDataInDb(restId, userId, currentTime, true);
        } else {
            checkInDbHelper.saveCheckInDataInDb(restId, userId, currentTime, true);
        }
        CheckInResponse checkInResponse = (CheckInResponse) value;
        if (null != checkInResponse) {
            SnapXToast.showToast(this, "Checked In successfully");
        }
    }

    private void getCheckedInData() {
        SharedPreferences preferences = utility.getSharedPreferences();
        userId = preferences.getString(getString(R.string.user_id), "");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.format_checkedIn));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        currentTime = simpleDateFormat.format(calendar.getTime());
    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {

    }

    @Override
    public void networkError(Object value) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!isCheckedIn) {
            startTimerForCheckIn();
        }
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isCheckedIn) {
            startTimerForCheckIn();
        }
    }

    private void startTimerForCheckIn() {
        Intent intent = new Intent(getActivity(), CheckInNotificationReceiver.class);
        intent.putExtra(getString(R.string.intent_restaurant_id), restId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), CHECKIN_NOTIFICATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                TimeUnit.HOURS.toMillis(CHECKIN_NOTIFICATION_TIME), pendingIntent);
    }

}
